package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.ChapterRequest;
import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.request.SubjectRequest;
import com.binar.pedulibelajar.dto.response.*;
import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.ERole;
import com.binar.pedulibelajar.enumeration.Type;
import com.binar.pedulibelajar.model.Category;
import com.binar.pedulibelajar.model.Chapter;
import com.binar.pedulibelajar.model.Course;
import com.binar.pedulibelajar.model.Subject;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.repository.CategoryRepository;
import com.binar.pedulibelajar.repository.ChapterRepository;
import com.binar.pedulibelajar.repository.CourseRepository;
import com.binar.pedulibelajar.repository.SubjectRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<DashboardCourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::mapToDashboardCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DetailCourseResponse getCourseByCourseCode(String courseCode) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Course> course = courseRepository.findByCourseCode(courseCode);

        if(userRepository.findByEmail(email).get().getRole() == ERole.ADMIN) {
            return course.map(this::mapToDetailCourseResponse).get();
        }

        if (course.get().getType().equals(Type.GRATIS)) {
            if (userCourseService.hasUserPurchasedFreeCourse(email, courseCode)) {
                return course.map(this::mapToDetailCourseResponse).get();
            }
            orderService.orderFree(courseCode);
        }

        if (userCourseService.hasUserPurchasedPremiumCourse(email, courseCode)) {
            return course.map(this::mapToDetailCourseResponse).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        } else {
            return course.map(this::mapToDetailCourseSubjectFreeResponse).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found2"));
        }
    }

    @Override
    public PaginationCourseResponse<DashboardCourseResponse> getCourseByFilters(Integer page, Integer size,
            List<CourseCategory> category,
            List<CourseLevel> levels, List<Type> types, String title) {
        page -= 1;
        Pageable pages = PageRequest.of(page, size);
        Page<Course> courses = courseRepository.findAllByFilters(category, levels, types, title, pages)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        return mapToPaginationCourseResponse(courses);
    }

    @Override
    public PaginationCourseResponse<DashboardMyCourseResponse> getMyCourse(Integer page, Integer size,
            List<CourseCategory> categories,
            List<CourseLevel> levels, List<Type> types, Boolean completed, String title) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        page -= 1;
        Pageable pages = PageRequest.of(page, size);
        Page<Course> courses = courseRepository
                .findMyCourseByFilters(categories, levels, types, completed, title, email, pages)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));
        return mapToPaginationMyCourseResponse(courses);
    }

    @Override
    public CourseResponse createCourse(CourseRequest courseRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Course course = mapToEntityCourse(courseRequest);
        course.setTeacher(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"))
                .getFullName());
        courseRepository.save(course);

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
        return modelMapper.map(course, CourseResponse.class);
    }

    @Override
    public CourseResponse updateCourse(String courseCode, CourseRequest courseRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Course existingCourse = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "course not found"));

        Course updateCourse = mapToEntityCourse(courseRequest);

        existingCourse.setTeacher(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"))
                .getFullName());
        existingCourse.setTitle(updateCourse.getTitle() != null ? updateCourse.getTitle() : existingCourse.getTitle());
        existingCourse.setCourseCode(updateCourse.getCourseCode() != null ? updateCourse.getCourseCode() : existingCourse.getCourseCode());
        existingCourse.setLevel(updateCourse.getLevel() != null ? updateCourse.getLevel() : existingCourse.getLevel());
        existingCourse.setType(updateCourse.getType() != null ? updateCourse.getType() : existingCourse.getType());
        existingCourse.setCategory(categoryRepository.findByCategoryName(updateCourse.getCategory().getCategoryName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "category not found")));
        existingCourse.setDescription(updateCourse.getDescription() != null ? updateCourse.getDescription() : existingCourse.getDescription());
        existingCourse.setPrice(updateCourse.getPrice() != null ? updateCourse.getPrice() : existingCourse.getPrice());
        existingCourse.setTelegramLink(updateCourse.getTelegramLink() != null ? updateCourse.getTelegramLink() : existingCourse.getTelegramLink());
        courseRepository.save(existingCourse);

        List<Chapter> chapters = existingCourse.getChapter();
        for (int i = 0; i < chapters.size(); i++) {
            Chapter chapter = chapters.get(i);
            Chapter updateChapter = updateCourse.getChapter().get(i);
            chapter.setCourse(existingCourse);
            chapter.setChapterNo(updateChapter.getChapterNo());
            chapter.setChapterTitle(updateChapter.getChapterTitle());
            chapterRepository.save(chapter);

            List<Subject> subjects = chapter.getSubject();
            for (int j = 0; j < subjects.size(); j++) {
                Subject subject = subjects.get(j);
                Subject updateSubject = updateCourse.getChapter().get(i).getSubject().get(j);
                subject.setChapter(chapter);
                subject.setSubjectNo(updateSubject.getSubjectNo());
                subject.setSubjectType(updateSubject.getSubjectType());
                subject.setVideoTitle(updateSubject.getVideoTitle());
                subject.setVideoLink(updateSubject.getVideoLink());
                subjectRepository.save(subject);
            }
        }
        return modelMapper.map(courseRequest, CourseResponse.class);
    }

    @Override
    public void deleteCourse(String courseCode) {
        courseRepository.findByCourseCode(courseCode).ifPresent(course -> {
            course.setDelete(true);
            courseRepository.save(course);
        });
    }

    @Override
    public long getTotalCourse() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return courseRepository.countTotalCourses(user.getFullName());
    }

    @Override
    public long getPremiumCourse() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return courseRepository.countPremiumCourses(user.getFullName());
    }

    @Override
    public List<CourseResponse> getManageCourses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return courseRepository.findManageCourses(user.getFullName()).stream()
                .map(course -> modelMapper.map(course, CourseResponse.class))
                .collect(Collectors.toList());
    }

    private DetailCourseResponse mapToDetailCourseResponse(Course course) {
        DetailCourseResponse response = new DetailCourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setCourseCode(course.getCourseCode());
        response.setCategory(mapToCategoryResponse(course.getCategory()));
        response.setType(course.getType());
        response.setLevel(course.getLevel());
        response.setPrice(course.getPrice());
        response.setDescription(course.getDescription());
        response.setTeacher(course.getTeacher());
        response.setModul(course.getChapter().size());
        response.setRating(course.getRating());
        response.setTelegramLink((course.getTelegramLink()));
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
        response.setCategory(mapToCategoryResponse(course.getCategory()));
        response.setType(course.getType());
        response.setLevel(course.getLevel());
        response.setPrice(course.getPrice());
        response.setDescription(course.getDescription());
        response.setTeacher(course.getTeacher());
        response.setModul(course.getChapter().size());
        response.setRating(course.getRating());
        response.setTelegramLink("-");
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
        response.setTitle(course.getTitle());
        response.setCategory(mapToCategoryResponse(course.getCategory()));
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
        course.setCategory(categoryRepository.findByCategoryName(courseRequest.getCategory().getCategoryName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "category not found")));
        course.setType(courseRequest.getType());
        course.setLevel(courseRequest.getLevel());
        course.setPrice(courseRequest.getPrice());
        course.setDescription(courseRequest.getDescription());
//        course.setTeacher(courseRequest.getTeacher());
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

    private PaginationCourseResponse<DashboardCourseResponse> mapToPaginationCourseResponse(Page<Course> coursePage) {
        List<Course> courseResponses = coursePage.getContent();

        List<DashboardCourseResponse> courseResponse = courseResponses.stream()
                .map(this::mapToDashboardCourseResponse)
                .collect(Collectors.toList());

        return PaginationCourseResponse.<DashboardCourseResponse>builder()
                .courses(courseResponse)
                .currentPage(coursePage.getNumber() + 1)
                .totalPage(coursePage.getTotalPages())
                .totalCourse(coursePage.getTotalElements())
                .build();
    }

    private PaginationCourseResponse<DashboardMyCourseResponse> mapToPaginationMyCourseResponse(
            Page<Course> coursePage) {
        List<Course> courseResponses = coursePage.getContent();

        List<DashboardMyCourseResponse> courseResponse = courseResponses.stream()
                .map(this::mapToDashboardMyCourseResponse)
                .collect(Collectors.toList());

        return PaginationCourseResponse.<DashboardMyCourseResponse>builder()
                .courses(courseResponse)
                .currentPage(coursePage.getNumber() + 1)
                .totalPage(coursePage.getTotalPages())
                .totalCourse(coursePage.getTotalElements())
                .build();
    }

    private DashboardMyCourseResponse mapToDashboardMyCourseResponse(Course course) {
        DashboardMyCourseResponse response = new DashboardMyCourseResponse();
        response.setCourseCode(course.getCourseCode());
        response.setTitle(course.getTitle());
        response.setCategory(mapToCategoryResponse(course.getCategory()));
        response.setLevel(course.getLevel());
        response.setPercentProgress(course.getUserProgresses().get(0).getPercent());
        response.setTeacher(course.getTeacher());
        response.setModul(course.getChapter().size());
        response.setRating(course.getRating());
        return response;
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .categoryImage(category.getCategoryImage())
                .build();
    }

}
