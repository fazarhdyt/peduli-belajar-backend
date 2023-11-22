package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    // Rename the method to avoid clash
    User findByUsernameIgnoreCase(String username);
}