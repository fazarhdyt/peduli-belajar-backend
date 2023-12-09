package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.TokenResetPassword;
import com.binar.pedulibelajar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenResetPasswordRepository extends JpaRepository<TokenResetPassword, Long> {

    Optional<TokenResetPassword> findByToken(String token);

    TokenResetPassword findByUserId(String id);

    boolean existsByToken(String token);

    @Modifying
    void deleteByUser(User user);
}
