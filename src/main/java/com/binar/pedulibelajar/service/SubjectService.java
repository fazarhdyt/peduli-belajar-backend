package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.SubjectRequest;
import com.binar.pedulibelajar.dto.response.SubjectResponse;

public interface SubjectService {

    SubjectResponse createSubject(String chapterId, SubjectRequest subjectRequest);

    SubjectResponse getSubject(String id);

    SubjectResponse updateSubject(String id, SubjectRequest subjectRequest);
}
