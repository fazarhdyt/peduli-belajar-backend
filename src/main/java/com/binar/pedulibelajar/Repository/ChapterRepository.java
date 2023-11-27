package com.binar.pedulibelajar.Repository;

import com.binar.pedulibelajar.Model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, String> {
    List<Chapter> findByCourseId(String courseId);
    Chapter findByCourseIdAndChapterNo(Long courseId, int chapterNo);
}
