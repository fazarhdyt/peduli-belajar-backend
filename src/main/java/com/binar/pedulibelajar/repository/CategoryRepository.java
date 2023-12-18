package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(CourseCategory categoryName);
}
