package com.binar.pedulibelajar.service;

public interface UserCourseService {

    boolean hasUserPurchasedPremiumCourse(String userEmail, String courseCode);

    boolean hasUserPurchasedFreeCourse(String userEmail, String courseCode);
}
