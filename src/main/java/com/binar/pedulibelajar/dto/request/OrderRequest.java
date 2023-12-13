package com.binar.pedulibelajar.dto.request;

import com.binar.pedulibelajar.enumeration.PaymentMethod;
import lombok.Data;

@Data
public class OrderRequest {

    String courseCode;
    PaymentMethod paymentMethod;
}
