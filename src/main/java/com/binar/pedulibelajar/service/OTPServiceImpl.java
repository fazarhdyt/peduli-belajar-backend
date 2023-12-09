package com.binar.pedulibelajar.service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import com.binar.pedulibelajar.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.binar.pedulibelajar.model.OTP;
import com.binar.pedulibelajar.repository.OTPRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OTPServiceImpl implements OTPService {

    @Value("${pedulibelajar.otpExpirationMs}")
    private Long otpDurationMs;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<OTP> findByOtp(String otp) {
        return otpRepository.findByOtp(otp);
    }

    @Override
    public OTP createOTP(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        OTP otp = otpRepository.findByUserId(user.getId());

        if (otp == null) {
            otp = new OTP();
        }

        Random rnd = new Random();
        int randomOtp = rnd.nextInt(999999);

        otp.setUser(user);
        otp.setExpiryDate(Instant.now().plusMillis(otpDurationMs));
        otp.setOtp(String.format("%06d", randomOtp));

        otpRepository.save(otp);
        return otp;
    }

    @Override
    public OTP verifyExpiration(OTP otp) {
        if (otp.getExpiryDate().compareTo(Instant.now()) < 0) {
            otpRepository.delete(otp);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP was expired");
        }
        return otp;
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        otpRepository.deleteByUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }
}
