package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.model.TokenResetPassword;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.repository.TokenResetPasswordRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class TokenResetPasswordImpl implements TokenResetPasswordService {

    @Value("${pedulibelajar.tokenExpirationMs}")
    private Long tokenDurationMs;

    @Autowired
    private TokenResetPasswordRepository tokenResetPasswordRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<TokenResetPassword> findByToken(String token) {
        return tokenResetPasswordRepository.findByToken(token);
    }

    @Override
    public TokenResetPassword createToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        TokenResetPassword tokenResetPassword = tokenResetPasswordRepository.findByUserId(user.getId());

        if (tokenResetPassword == null) {
            tokenResetPassword = new TokenResetPassword();
        }

        String randomTokenReset = RandomStringUtils.randomAlphanumeric(27);

        tokenResetPassword.setUser(user);
        tokenResetPassword.setExpiryDate(Instant.now().plusMillis(tokenDurationMs));
        tokenResetPassword.setToken(randomTokenReset);

        tokenResetPasswordRepository.save(tokenResetPassword);
        return tokenResetPassword;
    }

    @Override
    public TokenResetPassword verifyExpiration(TokenResetPassword tokenResetPassword) {
        if (tokenResetPassword.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenResetPasswordRepository.delete(tokenResetPassword);
            throw new RuntimeException("link reset password was expired");
        }
        return tokenResetPassword;
    }

    @Override
    public void deleteByEmail(String email) {
        tokenResetPasswordRepository.deleteByUser(userRepository.findByEmail(email).get());
    }
}
