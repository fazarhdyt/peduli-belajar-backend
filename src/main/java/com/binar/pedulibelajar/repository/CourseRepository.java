package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import com.binar.pedulibelajar.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

        Optional<Course> findByCourseCode(String courseCode);

        // TODO: query isDone pada course yang sudah dibeli user
        @Query("SELECT c FROM Course c WHERE " +
                        "(c.category IN (:category) OR :category IS NULL) AND " +
                        "(c.level IN (:level) OR :level IS NULL) AND " +
                        "(c.type IN (:type) OR :type IS NULL) AND " +
                        "(:title IS NULL OR (LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))))")
        Optional<Page<Course>> findAllByFilters(
                        @Param("category") List<CourseCategory> category,
                        @Param("level") List<CourseLevel> level,
                        @Param("type") List<Type> type,
                        @Param("title") String title,
                        Pageable pageable);

        @Query("SELECT COUNT(c) FROM Course c")
        long countTotalCourses();

        @Query("SELECT COUNT(c) FROM Course c WHERE c.type = 'PREMIUM'")
        long countPremiumCourses();

}