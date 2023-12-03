package com.binar.pedulibelajar.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String otp;

    @Column(nullable = false)
    private Instant expiryDate;
}
