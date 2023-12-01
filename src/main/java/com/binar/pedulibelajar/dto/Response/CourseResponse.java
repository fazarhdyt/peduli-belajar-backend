package com.binar.pedulibelajar.dto.Response;

import lombok.Data;

@Data
public class CourseResponse {
    private int code;
    private String status;
    private CourseData data;

    @Data
    public static class CourseData {
        private String name;
        private String category;
        private String courseCode;
        private String type;
        private String level;
        private double price;
    }
}
