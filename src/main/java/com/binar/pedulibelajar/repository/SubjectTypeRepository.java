package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.SubjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectTypeRepository extends JpaRepository<SubjectType, Long> {
    Optional<SubjectType> findByName(String name);
}