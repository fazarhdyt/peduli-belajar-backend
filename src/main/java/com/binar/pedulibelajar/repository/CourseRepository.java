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

        @Query("SELECT c FROM Course c " +
                        "LEFT JOIN Order o ON c.id = o.course.id WHERE " +
                        "c.delete = false AND" +
                        "(c.category.categoryName IN (:category) OR :category IS NULL) AND " +
                        "(c.level IN (:level) OR :level IS NULL) AND " +
                        "(c.type IN (:type) OR :type IS NULL) AND " +
                        "(:title IS NULL OR (LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))))" +
                        "GROUP BY c.id ORDER BY " +
                        "CASE WHEN :sortByDate = true THEN MAX(c.createdAt) END DESC, " +
                        "CASE WHEN :sortByPurchaseCount = true THEN COUNT(o) END DESC")
        Optional<Page<Course>> findAllByFilters(
                        @Param("category") List<CourseCategory> categories,
                        @Param("level") List<CourseLevel> levels,
                        @Param("type") List<Type> types,
                        @Param("title") String title,
                        @Param("sortByDate") Boolean sortByDate,
                        @Param("sortByPurchaseCount") Boolean sortByPurchaseCount,
                        Pageable pageable);

        @Query("SELECT c FROM Course c " +
                        "LEFT JOIN Order o ON c.id = o.course.id " +
                        "LEFT JOIN UserProgress up ON c.id = up.course.id " +
                        "WHERE up.user.email = :email " +
                        "AND (c.category.categoryName IN (:category) OR :category IS NULL) " +
                        "AND (c.level IN (:level) OR :level IS NULL) " +
                        "AND (c.type IN (:type) OR :type IS NULL) " +
                        "AND (:completed IS NULL OR (up.percent = 100 AND :completed = true) OR (up.percent < 100 AND :completed = false))"
                        +
                        "AND (:title IS NULL OR (LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))))" +
                        "GROUP BY c.id ORDER BY " +
                        "CASE WHEN :sortByDate = true THEN MAX(o.createdAt) END DESC, " +
                        "CASE WHEN :sortByPurchaseCount = true THEN COUNT(o) END DESC")
        Optional<Page<Course>> findMyCourseByFilters(
                        @Param("category") List<CourseCategory> categories,
                        @Param("level") List<CourseLevel> levels,
                        @Param("type") List<Type> types,
                        @Param("completed") Boolean completed,
                        @Param("title") String title,
                        @Param("email") String email,
                        @Param("sortByDate") Boolean sortByDate,
                        @Param("sortByPurchaseCount") Boolean sortByPurchaseCount,
                        Pageable pageable);

        @Query("SELECT c FROM Course c WHERE c.teacher = :teacher AND c.delete = false"+
                " AND (c.type = :type OR :type IS NULL)")
        List<Course> findManageCourses(@Param("teacher") String teacher, @Param("type")Type type);

        @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher = :teacher AND c.delete = false")
        long countTotalCourses(@Param("teacher") String teacher);

        @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher = :teacher AND c.type = 'PREMIUM' AND c.delete = false")
        long countPremiumCourses(@Param("teacher") String teacher);

}