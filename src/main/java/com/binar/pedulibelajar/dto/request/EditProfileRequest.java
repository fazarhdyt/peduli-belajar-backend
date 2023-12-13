package com.binar.pedulibelajar.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {

    private String fullName;

    private String noTelp;

    private String city;

    private String country;

    private MultipartFile profilePicture;
}