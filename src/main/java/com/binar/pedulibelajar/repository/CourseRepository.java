package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByCategory(String category);
}
