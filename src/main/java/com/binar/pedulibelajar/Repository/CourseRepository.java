package com.binar.pedulibelajar.Repository;

import com.binar.pedulibelajar.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
    Course findByCourseCode(String courseCode);
}
