package com.binar.pedulibelajar.dto.request;

import lombok.Data;

@Data
public class OrderRequest {

    String email;
    String courseCode;
    String paymentMethod;
}
