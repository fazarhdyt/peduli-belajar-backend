package com.binar.pedulibelajar.Model.Response;

import lombok.Data;

@Data
public class CourseResponse {
    private Number code;
    private String status;
    private CourseData data;

    @Data
    public static class CourseData{
        private String name;
        private String category;
        private String courseCode;
        private String type;
        private String level;
        private long price;
    }
}
