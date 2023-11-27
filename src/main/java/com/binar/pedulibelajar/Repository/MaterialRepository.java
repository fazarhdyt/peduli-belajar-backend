package com.binar.pedulibelajar.Repository;

import com.binar.pedulibelajar.Model.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialType, String> {
    MaterialType findByMaterialTypeName(String materialTypeName);
}
