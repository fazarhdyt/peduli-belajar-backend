package com.binar.pedulibelajar.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class StatusOrderResponse {

    private String username;
    private String category;
    private String title;
    private String status;
    private String paymentMethod;
    private String paymentDate;
}
