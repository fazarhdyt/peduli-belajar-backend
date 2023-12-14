package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.*;
import com.binar.pedulibelajar.dto.response.JwtResponse;
import com.binar.pedulibelajar.model.ERole;
import com.binar.pedulibelajar.model.OTP;
import com.binar.pedulibelajar.model.TokenResetPassword;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.repository.OTPRepository;
import com.binar.pedulibelajar.repository.TokenResetPasswordRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import com.binar.pedulibelajar.security.jwt.JwtUtils;
import org.modelmapper.ModelMapper;
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

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailSenderServiceImpl senderService;
    private final OTPRepository otpRepository;
    private final OTPService otpService;
    private final TokenResetPasswordService resetPasswordService;
    private final TokenResetPasswordRepository tokenResetPasswordRepository;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, EmailSenderServiceImpl senderService, OTPRepository otpRepository, OTPService otpService, TokenResetPasswordService resetPasswordService, TokenResetPasswordRepository tokenResetPasswordRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.senderService = senderService;
        this.otpRepository = otpRepository;
        this.otpService = otpService;
        this.resetPasswordService = resetPasswordService;
        this.tokenResetPasswordRepository = tokenResetPasswordRepository;
    }


    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "account has not been verified");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new JwtResponse(jwt,
                userDetails.getEmail(),
                userDetails.getNoTelp(),
                userDetails.getRole().toString());
    }

    // New private method
    private void registerUserOrAdmin(SignupRequest signupRequest, ERole role) {
        boolean userExist = userRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if (userExist) {
            throw new RuntimeException(
                    String.format("user with email '%s' already exist", signupRequest.getEmail()));
        }

        User user = modelMapper.map(signupRequest, User.class);

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(role);

        userRepository.save(user);

        OTP otp = otpService.createOTP(user.getEmail());

        senderService.sendMailOtp(user.getEmail(), otp);
    }

    // Updated methods
    @Override
    public void registerUser(SignupRequest signupRequest) {
        registerUserOrAdmin(signupRequest, ERole.USER);
    }

    @Override
    public void registerAdmin(SignupRequest signupRequest) {
        registerUserOrAdmin(signupRequest, ERole.ADMIN);
    }
    @Override
    public void verifyAccount(String email, String otp) {

        if (!userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        if (!otpRepository.existsByOtp(otp)) {
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

    @Override
    public void regenerateOtp(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "email not found"));
        OTP otp = otpService.createOTP(user.getEmail());

        senderService.sendMailOtp(user.getEmail(), otp);
    }

    @Override
    @Async
    public void generateLinkResetPassword(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "email not found"));
        TokenResetPassword token = resetPasswordService.createToken(user.getEmail());

        senderService.sendMailLinkResetPassword(user.getEmail(), token);
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {

        if (!tokenResetPasswordRepository.existsByToken(token)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "link not valid");
        }

        resetPasswordService.findByToken(token)
                .map(resetPasswordService::verifyExpiration)
                .map(TokenResetPassword::getUser)
                .ifPresent(user -> {
                    if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password not match");
                    }
                    String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword());
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                    resetPasswordService.deleteByEmail(user.getEmail());
                });
    }

    @Override
    public User editProfile(String email, EditProfileRequest editProfileRequest) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        modelMapper.map(editProfileRequest, existingUser);
        return userRepository.save(existingUser);
    }

    @Override
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        // Mendapatkan pengguna yang saat ini logins
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(currentAuthentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Memvalidasi password lama
        if (!bCryptPasswordEncoder.matches(updatePasswordDTO.getOldPassword(), currentUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Old password is incorrect");
        }

        // Mengganti password lama dengan password baru
        currentUser.setPassword(bCryptPasswordEncoder.encode(updatePasswordDTO.getNewPassword()));
        userRepository.save(currentUser);
    }

}