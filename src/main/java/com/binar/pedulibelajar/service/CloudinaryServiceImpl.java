package com.binar.pedulibelajar.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile multipartFile) {

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}