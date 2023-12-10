package com.binar.pedulibelajar.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private final Cloudinary cloudinary;


    @Override
    public String upload(MultipartFile multipartFile) {
        try{
            Map<?, ?> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.asMap(multipartFile.getName(), multipartFile.getName()));
            return uploadResult.get("url").toString();
    } catch (IOException e){
            throw new RuntimeException("Failed to upload profile picture");
        }
    }
}
