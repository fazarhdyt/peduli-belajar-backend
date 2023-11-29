package com.binar.pedulibelajar.repository;

import com.binar.pedulibelajar.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, String> {
    List<Chapter> findByCourseId(String courseId);
    Chapter findByCourseIdAndChapterNo(Long courseId, int chapterNo);
}
