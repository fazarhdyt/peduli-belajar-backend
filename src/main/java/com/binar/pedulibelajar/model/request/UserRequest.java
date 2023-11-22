package com.binar.pedulibelajar.model.request;

public class UserRequest {
    private String userId;
    private String PilihanBelajar;

    public UserRequest() {
    }

    public UserRequest(String userId, String PilihanBelajar) {
        this.userId = userId;
        this.PilihanBelajar = PilihanBelajar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPilihanBelajar() {
        return PilihanBelajar;
    }

    public void setPilihanBelajar(String PilihanBelajar) {
        this.PilihanBelajar = PilihanBelajar;
    }
}