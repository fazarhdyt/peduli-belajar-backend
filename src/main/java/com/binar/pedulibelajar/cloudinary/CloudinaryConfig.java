package com.binar.pedulibelajar.cloudinary;

import com.binar.pedulibelajar.service.CloudinaryService;
import com.binar.pedulibelajar.service.CloudinaryServiceImpl;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary (ObjectUtils.asMap(
                "cloud_name", "pedulibelajar",
                "api_key", "931245915128513",
                "api_secret", "W4-2zNnA8R3spiSaM1LNBq53bNg"));
    }

    @Bean
    public CloudinaryService cloudinaryService(){
        return new CloudinaryServiceImpl(cloudinary());
    }

}
