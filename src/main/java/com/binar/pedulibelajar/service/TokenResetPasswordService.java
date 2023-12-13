package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.model.TokenResetPassword;

import java.util.Optional;

public interface TokenResetPasswordService {

    Optional<TokenResetPassword> findByToken(String token);

    TokenResetPassword createToken(String email);

    TokenResetPassword verifyExpiration(TokenResetPassword tokenResetPassword);

    void deleteByEmail(String email);
}
