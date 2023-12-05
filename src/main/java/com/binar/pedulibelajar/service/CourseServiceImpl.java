package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.ChapterRequest;
import com.binar.pedulibelajar.dto.request.SubjectRequest;
import com.binar.pedulibelajar.dto.response.ChapterResponse;
import com.binar.pedulibelajar.dto.response.CreateCourseResponse;
import com.binar.pedulibelajar.dto.response.SubjectResponse;
import com.binar.pedulibelajar.model.Chapter;
import com.binar.pedulibelajar.model.Course;
import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.response.CourseResponse;
import com.binar.pedulibelajar.model.Subject;
import com.binar.pedulibelajar.model.SubjectType;
import com.binar.pedulibelajar.repository.ChapterRepository;
import com.binar.pedulibelajar.repository.CourseRepository;
import com.binar.pedulibelajar.repository.SubjectRepository;
import com.binar.pedulibelajar.repository.SubjectTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectTypeRepository subjectTypeRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<CourseResponse> getAllCourses() {
       List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse getCourseByCourseCode(String courseCode) {
        Optional<Course> course = courseRepository.findByCourseCode(courseCode);
        return course.map(this::mapToCourseResponse).get();
    }

    @Override
    public CreateCourseResponse createCourse(CourseRequest courseRequest) {
        Course course = courseRepository.save(mapToEntityCourse(courseRequest));

        List<Chapter> chapters = course.getChapter();
        for (Chapter chapter : chapters) {
            chapter.setCourse(course);
            chapterRepository.save(chapter);
            List<Subject> subjects = chapter.getSubject();
            for (Subject subject : subjects) {
                subject.setChapter(chapter);
                subjectRepository.save(subject);
            }
        }
        return modelMapper.map(courseRequest, CreateCourseResponse.class);
    }

    @Override
    public CreateCourseResponse updateCourse(String courseCode, CourseRequest courseRequest) {
        return courseRepository.findByCourseCode(courseCode)
                .map(existingCourse -> {
                    existingCourse.setName(courseRequest.getName());
                    existingCourse.setCourseCode(courseRequest.getCourseCode());
                    existingCourse.setLevel(courseRequest.getLevel());
                    existingCourse.setType(courseRequest.getType());
                    existingCourse.setCategory(courseRequest.getCategory());
                    existingCourse.setDescription(courseRequest.getDescription());
                    existingCourse.setPrice(courseRequest.getPrice());
                    existingCourse.setAuthor(courseRequest.getAuthor());
                    Course updatedCourse = courseRepository.save(existingCourse);
                    return modelMapper.map(updatedCourse, CreateCourseResponse.class);
                }).orElse(null);
    }

    @Override
    public void deleteCourse(String courseCode) {
        courseRepository.findByCourseCode(courseCode).ifPresent(course ->
            courseRepository.delete(course));
    }

    private CourseResponse mapToCourseResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setName(course.getName());
        response.setCourseCode(course.getCourseCode());
        response.setCategory(course.getCategory());
        response.setType(course.getType());
        response.setLevel(course.getLevel());
        response.setPrice(course.getPrice());
        response.setDescription(course.getDescription());
        response.setAuthor(course.getAuthor());
        List<ChapterResponse> chapterResponses = course.getChapter().stream()
                .map(this::mapToChapterResponse)
                .collect(Collectors.toList());

        response.setChapter(chapterResponses);
        return response;
    }

    private ChapterResponse mapToChapterResponse(Chapter chapter) {
        ChapterResponse chapterResponse = new ChapterResponse();
        chapterResponse.setChapterNo(chapter.getChapterNo());
        chapterResponse.setChapterTitle(chapter.getChapterTitle());

        List<SubjectResponse> subjectResponses = chapter.getSubject().stream()
                .map(this::mapToSubjectResponse)
                .collect(Collectors.toList());

        chapterResponse.setSubject(subjectResponses);

        return chapterResponse;
    }

    private SubjectResponse mapToSubjectResponse(Subject subject) {
        SubjectResponse subjectResponse = new SubjectResponse();
        subjectResponse.setSubjectNo(subject.getSubjectNo());
        subjectResponse.setVideoTitle(subject.getVideoTitle());
        subjectResponse.setVideoLink(subject.getVideoLink());
        subjectResponse.setSubjectType(subject.getSubjectType().getName());

        return subjectResponse;
    }

    private Course mapToEntityCourse(CourseRequest courseRequest) {
        Course course = new Course();
        course.setName(courseRequest.getName());
        course.setCourseCode(courseRequest.getCourseCode());
        course.setCategory(courseRequest.getCategory());
        course.setType(courseRequest.getType());
        course.setLevel(courseRequest.getLevel());
        course.setPrice(courseRequest.getPrice());
        course.setDescription(courseRequest.getDescription());
        course.setAuthor(courseRequest.getAuthor());
        List<Chapter> chapter = courseRequest.getChapter().stream()
                .map(this::mapToEntityChapter)
                .collect(Collectors.toList());

        course.setChapter(chapter);
        return course;
    }

    private Chapter mapToEntityChapter(ChapterRequest chapterRequest) {
        Chapter chapter = new Chapter();
        chapter.setChapterNo(chapterRequest.getChapterNo());
        chapter.setChapterTitle(chapterRequest.getChapterTitle());

        List<Subject> subject = chapterRequest.getSubject().stream()
                .map(this::mapToEntitySubject)
                .collect(Collectors.toList());

        chapter.setSubject(subject);

        return chapter;
    }
    private Subject mapToEntitySubject(SubjectRequest subjectRequest) {
        Subject subject = new Subject();
        subject.setSubjectNo(subjectRequest.getSubjectNo());
        subject.setVideoTitle(subjectRequest.getVideoTitle());
        subject.setVideoLink(subjectRequest.getVideoLink());
        SubjectType subjectType = subjectTypeRepository.findByName(subjectRequest.getSubjectType().getName())
                .orElseThrow(() -> new EntityNotFoundException("SubjectType not found with name: " + subjectRequest.getSubjectType().getName()));
        subject.setSubjectType(subjectType);
        return subject;
    }

}
