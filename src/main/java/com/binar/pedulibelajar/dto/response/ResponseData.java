package com.binar.pedulibelajar.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseData {

    public static ResponseEntity<Object> statusResponse(Object data, HttpStatus status, String message) {
        Map<String, Object> bodyResponse = new HashMap<>();
        if (data != null) {
            bodyResponse.put("data", data);
        }
        bodyResponse.put("status", status.value());
        bodyResponse.put("message", message);
        return ResponseEntity.status(status).body(bodyResponse);
    }

    public static ResponseEntity<Object> internalServerError(String message) {
        return statusResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
