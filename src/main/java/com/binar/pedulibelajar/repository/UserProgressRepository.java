package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Course;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserAndCourse(User user, Course course);
}
