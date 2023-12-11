package com.binar.pedulibelajar.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageRequest {
    private MultipartFile multipartFile;
    private String fileName;
    private String uploaderName;
}
