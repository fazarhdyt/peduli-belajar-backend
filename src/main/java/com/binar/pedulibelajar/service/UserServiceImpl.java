package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.LoginRequest;
import com.binar.pedulibelajar.dto.request.SignupRequest;
import com.binar.pedulibelajar.dto.response.JwtResponse;
import com.binar.pedulibelajar.model.ERole;
import com.binar.pedulibelajar.model.OTP;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.repository.OTPRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import com.binar.pedulibelajar.security.jwt.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailSenderServiceImpl senderService;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private OTPService otpService;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        User user = modelMapper.map(loginRequest, User.class);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getEmail(),
                userDetails.getNoTelp(),
                userDetails.getRole().toString());
    }

    @Override
    @Async
    public User registerUser(SignupRequest signupRequest){
        boolean userExist = userRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if(userExist){
            throw new RuntimeException(
                    String.format("user with email '%s' already exist", signupRequest.getEmail()));
        }

        User user = modelMapper.map(signupRequest, User.class);

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(ERole.USER);

        userRepository.save(user);

        OTP otp = otpService.createOTP(user.getEmail());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        ZonedDateTime zonedDateTime = otp.getExpiryDate().atZone(ZoneId.systemDefault());

        String subject = "Account Verification";
        String body = "Your OTP : " + otp.getOtp() + " valid until " + formatter.format(zonedDateTime);
        senderService.sendMail(user.getEmail(), subject, body);

        return user;
    }

    @Override
    @Async
    public User registerAdmin(SignupRequest signupRequest){
        boolean userExist = userRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if(userExist){
            throw new RuntimeException(
                    String.format("user with email '%s' already exist", signupRequest.getEmail()));
        }

        User user = modelMapper.map(signupRequest, User.class);

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(ERole.ADMIN);

        userRepository.save(user);

        OTP otp = otpService.createOTP(user.getEmail());

        String subject = "Account Verification";
        String body = "Your OTP : " + otp.getOtp();
        senderService.sendMail(user.getEmail(), subject, body);

        return user;
    }

    @Override
    public void verifyAccount(String email, String otp) {

        if(!userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        if(!otpRepository.existsByOtp(otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP code");
        }

        otpService.findByOtp(otp)
                .map(otpService::verifyExpiration)
                .map(OTP::getUser)
                .ifPresent(user -> {
                    user.setActive(true);
                    userRepository.save(user);
                    otpService.deleteByEmail(user.getEmail());
                });
    }

}
