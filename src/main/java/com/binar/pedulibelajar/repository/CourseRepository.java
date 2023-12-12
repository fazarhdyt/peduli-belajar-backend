package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCourseCode(String courseCode);
    @Query("SELECT c FROM Course c WHERE " +
            "(c.category IN (:category) OR :category IS NULL) AND " +
            "(c.level IN (:level) OR :level IS NULL) AND " +
            "(c.type IN (:type) OR :type IS NULL)")
    Page<Course> findAllByFilters(@Param("category") List<String> category,
                                  @Param("level") List<String> level,
                                  @Param("type") List<String> type, Pageable pageable);


}