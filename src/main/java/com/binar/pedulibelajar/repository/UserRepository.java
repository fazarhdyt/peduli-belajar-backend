package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true AND u.role = 'USER'")
    long countActiveUsers();

}
