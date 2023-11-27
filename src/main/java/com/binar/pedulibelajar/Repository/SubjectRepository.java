package com.binar.pedulibelajar.Repository;

import com.binar.pedulibelajar.Model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, String> {
    List<Subject> findByChapterId(String chapterId);
}
