package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Course;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {

    Optional<UserCourse> findByUserIdAndSubjectId(String userId, String subjectId);

    List<UserCourse> findByUserAndCourse(User user, Course course);

    @Query("SELECT COUNT(uc) > 0 FROM UserCourse uc " +
            "WHERE uc.user = :user AND uc.course = :course AND uc.subjectId = :subjectId AND uc.done = true")
    boolean existsByUserAndCourseAndSubjectId(@Param("user") User user, @Param("course") Course course, @Param("subjectId") String subjectId);
}
