package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.ChapterRequest;
import com.binar.pedulibelajar.dto.request.EditChapterRequest;
import com.binar.pedulibelajar.dto.response.ChapterResponse;
import com.binar.pedulibelajar.model.Chapter;
import com.binar.pedulibelajar.model.Subject;
import com.binar.pedulibelajar.repository.ChapterRepository;
import com.binar.pedulibelajar.repository.CourseRepository;
import com.binar.pedulibelajar.repository.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService{

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChapterResponse createChapter(String courseId, ChapterRequest chapterRequest) {
        Chapter chapter = modelMapper.map(chapterRequest, Chapter.class);
        chapter.setCourse(courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
        chapterRepository.save(chapter);
        List<Subject> subjects = chapter.getSubject();
        for (Subject subject : subjects) {
            subject.setChapter(chapter);
            subjectRepository.save(subject);
        }
        return modelMapper.map(chapter, ChapterResponse.class);
    }

    @Override
    public ChapterResponse getChapter(String id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "chapter not found"));
        return modelMapper.map(chapter, ChapterResponse.class);
    }

    @Override
    public ChapterResponse updateChapter(String id, EditChapterRequest editChapterRequest) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "chapter not found"));
        chapter.setChapterNo(editChapterRequest.getChapterNo());
        chapter.setChapterTitle(editChapterRequest.getChapterTitle());
        chapterRepository.save(chapter);
        return modelMapper.map(chapter, ChapterResponse.class);
    }
}
