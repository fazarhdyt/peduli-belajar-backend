package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.SubjectRequest;
import com.binar.pedulibelajar.dto.response.SubjectResponse;
import com.binar.pedulibelajar.model.Subject;
import com.binar.pedulibelajar.repository.ChapterRepository;
import com.binar.pedulibelajar.repository.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public SubjectResponse createSubject(String chapterId, SubjectRequest subjectRequest) {
        Subject subject = modelMapper.map(subjectRequest, Subject.class);
        subject.setChapter(chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "chapter not found")));
        subjectRepository.save(subject);
        return modelMapper.map(subject, SubjectResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponse getSubject(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "subject not found"));
        return modelMapper.map(subject, SubjectResponse.class);
    }

    @Override
    @Transactional
    public SubjectResponse updateSubject(String id, SubjectRequest subjectRequest) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "subject not found"));
        subject.setChapter(chapterRepository.findById(subject.getChapter().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "chapter not found")));
        subject.setSubjectNo(subjectRequest.getSubjectNo());
        subject.setSubjectType(subjectRequest.getSubjectType());
        subject.setVideoTitle(subjectRequest.getVideoTitle());
        subject.setVideoLink(subjectRequest.getVideoLink());
        subjectRepository.save(subject);
        return modelMapper.map(subject, SubjectResponse.class);
    }
}
