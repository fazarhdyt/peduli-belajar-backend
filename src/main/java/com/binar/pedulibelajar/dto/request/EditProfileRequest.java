package com.binar.pedulibelajar.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EditProfileRequest {

    private String fullName;

    private String noTelp;

    private MultipartFile profilePicture;

    private String city;

    private String country;
}