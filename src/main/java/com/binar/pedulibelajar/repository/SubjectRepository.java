package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, String> {
    List<Subject> findByChapterId(String chapterId);
}