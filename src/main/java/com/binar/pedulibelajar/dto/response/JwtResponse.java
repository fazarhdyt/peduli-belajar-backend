package com.binar.pedulibelajar.dto.response;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String email;
    private String noTelp;
    private String role;

    public JwtResponse(String accessToken, String email, String noTelp, String role) {
        this.token = accessToken;
        this.email = email;
        this.noTelp = noTelp;
        this.role = role;
    }
}
