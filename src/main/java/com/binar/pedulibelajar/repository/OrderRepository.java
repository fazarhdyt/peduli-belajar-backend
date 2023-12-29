package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Course;
import com.binar.pedulibelajar.model.Order;
import com.binar.pedulibelajar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

        @Query(value = "SELECT EXISTS (" +
                        "SELECT 1 FROM orders o " +
                        "JOIN users u ON o.user_id = u.id " +
                        "JOIN course c ON o.course_id = c.id " +
                        "WHERE u.email = :email " +
                        "AND c.course_code = :courseCode)", nativeQuery = true)
        boolean existsUserOrder(@Param("email") String email, @Param("courseCode") String courseCode);

        @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
                        "FROM Order o " +
                        "WHERE o.user = :user " +
                        "AND o.course = :course " +
                        "AND o.paid = true")
        boolean isPaidByUserAndCourse(User user, Course course);

        @Query("SELECT COUNT(o) FROM Order o JOIN Course c ON o.course.id = c.id WHERE c.teacher = :teacher")
        long countActiveUsers(@Param("teacher") String teacher);

        @Query("SELECT o FROM Order o JOIN o.course c " +
                "WHERE c.teacher = :teacher " +
                "AND c.type = 'PREMIUM' " +
                "AND (:isPaid is null OR o.paid = :isPaid) " +
                "AND (:title IS NULL OR (LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))))")
        List<Order> findOrdersByTeacherAndIsPaid(@Param("teacher") String teacher,
                                                 @Param("isPaid") Boolean isPaid, @Param("title") String title);
}
