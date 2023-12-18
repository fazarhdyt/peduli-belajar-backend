package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
}
