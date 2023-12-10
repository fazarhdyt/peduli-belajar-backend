package com.binar.pedulibelajar.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    public String upload(MultipartFile multipartFile);
}
