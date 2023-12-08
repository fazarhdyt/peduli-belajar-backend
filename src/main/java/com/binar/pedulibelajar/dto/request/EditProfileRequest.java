package com.binar.pedulibelajar.dto.request;

import lombok.Data;

@Data
public class EditProfileRequest {

    private String fullName;

    private String noTelp;

    private String profilePicture;

    private String city;

    private String country;
}