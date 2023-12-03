package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.OTP;
import com.binar.pedulibelajar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {

    Optional<OTP> findByOtp(String otp);

    OTP findByUserId(String id);

    boolean existsByOtp(String otp);

    @Modifying
    void deleteByUser(User user);
}
