package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.ChapterRequest;
import com.binar.pedulibelajar.dto.request.EditChapterRequest;
import com.binar.pedulibelajar.dto.response.ChapterResponse;

public interface ChapterService {

    ChapterResponse createChapter(String courseId, ChapterRequest chapterRequest);

    ChapterResponse getChapter(String id);

    ChapterResponse updateChapter(String id, EditChapterRequest editChapterRequest);
}
