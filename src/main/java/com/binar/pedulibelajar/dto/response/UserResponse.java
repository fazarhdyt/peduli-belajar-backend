package com.binar.pedulibelajar.dto.response;

import lombok.Data;

@Data
public class UserResponse {

    private String id;
    private String fullName;
    private String email;
    private String noTelp;
    private String city;
    private String country;
    private String profilePictureUrl;
}
