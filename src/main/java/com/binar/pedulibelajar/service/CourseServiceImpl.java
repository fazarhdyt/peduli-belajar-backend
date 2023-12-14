package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.ChapterRequest;
import com.binar.pedulibelajar.dto.request.SubjectRequest;
import com.binar.pedulibelajar.dto.response.*;
import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import com.binar.pedulibelajar.model.*;
import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.repository.ChapterRepository;
import com.binar.pedulibelajar.repository.CourseRepository;
import com.binar.pedulibelajar.repository.SubjectRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<DetailCourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::mapToDetailCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DetailCourseResponse getCourseByCourseCode(String courseCode) {
        Optional<Course> course = courseRepository.findByCourseCode(courseCode);
        if (course.get().getType().equals(Type.FREE)) {
            return course.map(this::mapToDetailCourseResponse).get();
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userCourseService.hasUserPurchasedCourse(email, courseCode)) {
            return course.map(this::mapToDetailCourseResponse).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        } else {
            return course.map(this::mapToDetailCourseSubjectFreeResponse).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found2"));
        }
    }

    @Override
    public PaginationCourseResponse getCourseByFilters(Integer page, Integer size, List<CourseCategory> category,
            List<CourseLevel> levels, List<Type> types, String title) {
        page -= 1;
        Pageable pages = PageRequest.of(page, size);
        Page<Course> courses = courseRepository.findAllByFilters(category, levels, types, title, pages)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        ;
        return mapToPaginationCourseResponse(courses);
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
        /*
         * TODO: FIXING UPLOAD THUMBNAIL
         * if (courseRequest != null && !courseRequest.getThumbnail().isEmpty()) {
         * try {
         * Map<?, ?> uploadResult =
         * cloudinary.uploader().upload(courseRequest.getThumbnail().getBytes(),
         * ObjectUtils.emptyMap());
         * String imageUrl = uploadResult.get("url").toString();
         * course.setThumbnail(imageUrl);
         * } catch (IOException e) {
         * e.printStackTrace();
         * }
         * }
         * 
         */

        return modelMapper.map(courseRequest, CreateCourseResponse.class);
    }

    @Override
    public CreateCourseResponse updateCourse(String courseCode, CourseRequest courseRequest) {
        /*
         * TODO: FIXING UPLOAD THUMBNAIL
         * Course course = courseRepository.findByCourseCode(courseCode)
         * .orElseThrow(() -> new RuntimeException("Course not found"));
         * if (courseRequest != null && !courseRequest.getThumbnail().isEmpty()) {
         * try {
         * Map<?, ?> uploadResult =
         * cloudinary.uploader().upload(courseRequest.getThumbnail().getBytes(),
         * ObjectUtils.emptyMap());
         * String imageUrl = uploadResult.get("url").toString();
         * course.setThumbnail(imageUrl);
         * } catch (IOException e) {
         * e.printStackTrace();
         * }
         * }
         * 
         */

        return courseRepository.findByCourseCode(courseCode)
                .map(existingCourse -> {
                    existingCourse.setTitle(courseRequest.getTitle());
                    existingCourse.setCourseCode(courseRequest.getCourseCode());
                    existingCourse.setLevel(courseRequest.getLevel());
                    existingCourse.setType(courseRequest.getType());
                    existingCourse.setCategory(courseRequest.getCategory());
                    existingCourse.setDescription(courseRequest.getDescription());
                    existingCourse.setPrice(courseRequest.getPrice());
                    existingCourse.setTeacher(courseRequest.getTeacher());
                    // existingCourse.setThumbnail(String.valueOf(courseRequest.getThumbnail()));
                    Course updatedCourse = courseRepository.save(existingCourse);
                    return modelMapper.map(updatedCourse, CreateCourseResponse.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
    }

    @Override
    public void deleteCourse(String courseCode) {
        courseRepository.findByCourseCode(courseCode).ifPresent(course -> courseRepository.delete(course));
    }

    @Override
    public long getTotalCourse() {
        return courseRepository.countTotalCourses();
    }

    @Override
    public long getPremiumCourse() {
        return courseRepository.countPremiumCourses();
    }

    private DetailCourseResponse mapToDetailCourseResponse(Course course) {
        DetailCourseResponse response = new DetailCourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setCourseCode(course.getCourseCode());
        response.setCategory(course.getCategory());
        response.setType(course.getType());
        response.setLevel(course.getLevel());
        response.setPrice(course.getPrice());
        response.setDescription(course.getDescription());
        response.setTeacher(course.getTeacher());
        response.setModul(course.getChapter().size());
        response.setRating(course.getRating());
        List<ChapterResponse> chapterResponses = course.getChapter().stream()
                .map(this::mapToChapterResponse)
                .collect(Collectors.toList());

        response.setChapter(chapterResponses);
        return response;
    }

    private DetailCourseResponse mapToDetailCourseSubjectFreeResponse(Course course) {
        DetailCourseResponse response = new DetailCourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setCourseCode(course.getCourseCode());
        response.setCategory(course.getCategory());
        response.setType(course.getType());
        response.setLevel(course.getLevel());
        response.setPrice(course.getPrice());
        response.setDescription(course.getDescription());
        response.setTeacher(course.getTeacher());
        response.setModul(course.getChapter().size());
        response.setRating(course.getRating());
        List<ChapterResponse> chapterResponses = course.getChapter().stream()
                .map(this::mapToChapterSubjectFreeResponse)
                .collect(Collectors.toList());

        response.setChapter(chapterResponses);
        return response;
    }

    private ChapterResponse mapToChapterSubjectFreeResponse(Chapter chapter) {
        ChapterResponse chapterResponse = new ChapterResponse();
        chapterResponse.setChapterNo(chapter.getChapterNo());
        chapterResponse.setChapterTitle(chapter.getChapterTitle());

        List<SubjectResponse> subjectResponses = chapter.getSubject().stream()
                .map(this::mapToSubjectFreeResponse)
                .collect(Collectors.toList());

        chapterResponse.setSubject(subjectResponses);

        return chapterResponse;
    }

    private SubjectResponse mapToSubjectFreeResponse(Subject subject) {
        SubjectResponse subjectResponse = new SubjectResponse();
        subjectResponse.setSubjectNo(subject.getSubjectNo());
        subjectResponse.setVideoTitle(subject.getVideoTitle());
        subjectResponse.setVideoLink(subject.getSubjectType().equals(Type.PREMIUM) ? "" : subject.getVideoLink());
        subjectResponse.setSubjectType(subject.getSubjectType());

        return subjectResponse;
    }

    private DashboardCourseResponse mapToDashboardCourseResponse(Course course) {
        DashboardCourseResponse response = new DashboardCourseResponse();
        response.setCourseCode(course.getCourseCode());
        response.setThumbnail(course.getThumbnail());
        response.setTitle(course.getTitle());
        response.setCategory(course.getCategory());
        response.setLevel(course.getLevel());
        response.setPrice(course.getPrice());
        response.setTeacher(course.getTeacher());
        response.setModul(course.getChapter().size());
        response.setRating(course.getRating());
        return response;
    }

    private ChapterResponse mapToChapterResponse(Chapter chapter) {
        ChapterResponse chapterResponse = new ChapterResponse();
        chapterResponse.setId(chapter.getId());
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
        subjectResponse.setId(subject.getId());
        subjectResponse.setSubjectNo(subject.getSubjectNo());
        subjectResponse.setVideoTitle(subject.getVideoTitle());
        subjectResponse.setVideoLink(subject.getVideoLink());
        subjectResponse.setSubjectType(subject.getSubjectType());

        return subjectResponse;
    }

    private Course mapToEntityCourse(CourseRequest courseRequest) {
        Course course = new Course();
        course.setTitle(courseRequest.getTitle());
        course.setCourseCode(courseRequest.getCourseCode());
        course.setCategory(courseRequest.getCategory());
        course.setType(courseRequest.getType());
        course.setLevel(courseRequest.getLevel());
        course.setPrice(courseRequest.getPrice());
        course.setDescription(courseRequest.getDescription());
        course.setTeacher(courseRequest.getTeacher());
        course.setRating(0);
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
        subject.setSubjectType(subjectRequest.getSubjectType());
        return subject;
    }

    private PaginationCourseResponse mapToPaginationCourseResponse(Page<Course> coursePage) {
        List<Course> courseResponses = coursePage.getContent();

        List<DashboardCourseResponse> courseResponse = courseResponses.stream()
                .map(this::mapToDashboardCourseResponse)
                .collect(Collectors.toList());

        return PaginationCourseResponse.builder()
                .courses(courseResponse)
                .currentPage(coursePage.getNumber() + 1)
                .totalPage(coursePage.getTotalPages())
                .totalCourse(coursePage.getTotalElements())
                .build();
    }

}
