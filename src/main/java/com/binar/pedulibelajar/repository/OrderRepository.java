package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Course;
import com.binar.pedulibelajar.model.Order;
import com.binar.pedulibelajar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 FROM orders o " +
            "JOIN users u ON o.user_id = u.id " +
            "JOIN course c ON o.course_id = c.id " +
            "WHERE u.email = :email " +
            "AND c.course_code = :courseCode)",
            nativeQuery = true)
    boolean existsUserOrder(@Param("email") String email, @Param("courseCode") String courseCode);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM Order o " +
            "WHERE o.user = :user " +
            "AND o.course = :course " +
            "AND o.paid = true")
    boolean isPaidByUserAndCourse(User user, Course course);
}
