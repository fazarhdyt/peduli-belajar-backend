package com.binar.pedulibelajar.repository;

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

        @Query("SELECT c FROM Course c WHERE " +
                        "(:category IS NULL OR c.category IN (:category)) AND " +
                        "(:level IS NULL OR c.level IN (:level)) AND " +
                        "(:type IS NULL OR c.type IN (:type))")
        Page<Course> findAllByFilters(@Param("category") List<String> category,
                        @Param("level") List<String> level,
                        @Param("type") List<String> type, Pageable pageable);

}