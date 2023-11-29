package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.SubjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectTypeRepository extends JpaRepository<SubjectType, String> {
    SubjectType findByName(String name);
}
