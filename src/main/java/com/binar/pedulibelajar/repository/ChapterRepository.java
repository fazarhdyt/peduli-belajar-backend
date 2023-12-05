package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    List<Chapter> findByCourseId(String courseId);
    Chapter findByCourseIdAndChapterNo(Long courseId, int chapterNo);
}
