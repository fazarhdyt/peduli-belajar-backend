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

        @Query("SELECT c FROM Course c " +
                "JOIN UserProgress up ON c.id = up.course.id " +
                "JOIN Order o ON c.id = o.course.id " +
                "WHERE up.user.email = :email " +
                "AND (c.category IN (:category) OR :category IS NULL) " +
                "AND (c.level IN (:level) OR :level IS NULL) " +
                "AND (c.type IN (:type) OR :type IS NULL) " +
                "AND (:progress IS NULL OR (up.percent = 100 AND :progress = 'done') OR (up.percent < 100 AND :progress = 'in progress'))" +
                "AND (:title IS NULL OR (LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))))")
        Optional<Page<Course>> findMyCourseByFilters(
                @Param("category") List<CourseCategory> categories,
                @Param("level") List<CourseLevel> levels,
                @Param("type") List<Type> types,
                @Param("progress") String progress,
                @Param("title") String title,
                @Param("email") String email,
                Pageable pageable);

        @Query("SELECT COUNT(c) FROM Course c")
        long countTotalCourses();

        @Query("SELECT COUNT(c) FROM Course c WHERE c.type = 'PREMIUM'")
        long countPremiumCourses();

}