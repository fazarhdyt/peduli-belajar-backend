package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialType, String> {
    MaterialType findByMaterialTypeName(String materialTypeName);
}
