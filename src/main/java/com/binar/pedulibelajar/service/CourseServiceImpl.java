package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.ChapterRequest;
import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.request.EditCourseRequest;
import com.binar.pedulibelajar.dto.request.SubjectRequest;
import com.binar.pedulibelajar.dto.response.*;
import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.ERole;
import com.binar.pedulibelajar.enumeration.Type;
import com.binar.pedulibelajar.model.*;
import com.binar.pedulibelajar.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

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
        Optional<Course> courseOptional = courseRepository.findByCourseCode(courseCode);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        if(user.getRole() == ERole.ADMIN) {
            return courseOptional.map(course -> mapToDetailCourseResponse(course, null)).get();
        }

        if (courseOptional.get().getType().equals(Type.GRATIS)) {
            if (userCourseService.hasUserPurchasedFreeCourse(email, courseCode)) {
                return courseOptional.map(course -> mapToDetailCourseResponse(course, user)).get();
            }
            orderService.orderFree(courseCode);
        }

        if (userCourseService.hasUserPurchasedPremiumCourse(email, courseCode)) {
            return courseOptional.map(course -> mapToDetailCourseResponse(course, user)).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        } else {
            return courseOptional.map(course -> mapToDetailCourseSubjectFreeResponse(course, user)).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
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
    public CourseResponse updateCourse(String courseCode, EditCourseRequest editCourseRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Course existingCourse = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "course not found"));

        Course updateCourse = mapToEntityEditCourse(editCourseRequest);

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

        return modelMapper.map(editCourseRequest, CourseResponse.class);
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

    @Override
    public Map<String, Double> getProgress(String courseCode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "course not found"));
        Map<String, Double> response = new HashMap<>();
        response.put("percent", userProgressRepository.findByUserAndCourse(user, course)
                .map(UserProgress::getPercent)
                .orElse(0.0));
        return response;
    }

    private DetailCourseResponse mapToDetailCourseResponse(Course course, User user) {
        DetailCourseResponse response = modelMapper.map(course, DetailCourseResponse.class);
        response.setCategory(mapToCategoryResponse(course.getCategory()));
        response.setModul(course.getChapter().size());
        List<ChapterResponse> chapterResponses = course.getChapter().stream()
                .map(chapter -> mapToChapterResponse(chapter, user, course))
                .collect(Collectors.toList());

        response.setChapter(chapterResponses);
        return response;
    }

    private ChapterResponse mapToChapterResponse(Chapter chapter, User user, Course course) {
        ChapterResponse chapterResponse = modelMapper.map(chapter, ChapterResponse.class);

        List<SubjectResponse> subjectResponses = chapter.getSubject().stream()
                .map(subject -> mapToSubjectResponse(subject, user, course))
                .collect(Collectors.toList());

        chapterResponse.setSubject(subjectResponses);

        return chapterResponse;
    }

    private SubjectResponse mapToSubjectResponse(Subject subject, User user, Course course) {
        SubjectResponse subjectResponse = modelMapper.map(subject, SubjectResponse.class);
        subjectResponse.setDone(isSubjectDone(subject, user, course));

        return subjectResponse;
    }

    private boolean isSubjectDone(Subject subject, User user, Course course) {
        return userCourseRepository.existsByUserAndCourseAndSubjectId(user, course, subject.getId());
    }

    private DetailCourseResponse mapToDetailCourseSubjectFreeResponse(Course course, User user) {
        DetailCourseResponse response = modelMapper.map(course, DetailCourseResponse.class);
        response.setCategory(mapToCategoryResponse(course.getCategory()));
        response.setModul(course.getChapter().size());
        response.setTelegramLink("-");
        List<ChapterResponse> chapterResponses = course.getChapter().stream()
                .map(chapter -> mapToChapterSubjectFreeResponse(chapter, user, course))
                .collect(Collectors.toList());

        response.setChapter(chapterResponses);
        return response;
    }

    private ChapterResponse mapToChapterSubjectFreeResponse(Chapter chapter, User user, Course course) {
        ChapterResponse chapterResponse = modelMapper.map(chapter, ChapterResponse.class);

        List<SubjectResponse> subjectResponses = chapter.getSubject().stream()
                .map(subject -> mapToSubjectFreeResponse(subject, user, course))
                .collect(Collectors.toList());

        chapterResponse.setSubject(subjectResponses);

        return chapterResponse;
    }

    private SubjectResponse mapToSubjectFreeResponse(Subject subject, User user, Course course) {
        SubjectResponse subjectResponse = modelMapper.map(subject, SubjectResponse.class);
        subjectResponse.setVideoLink(subject.getSubjectType().equals(Type.PREMIUM) ? "" : subject.getVideoLink());
        subjectResponse.setDone(isSubjectDone(subject, user, course));

        return subjectResponse;
    }

    private DashboardCourseResponse mapToDashboardCourseResponse(Course course) {
        DashboardCourseResponse response = modelMapper.map(course, DashboardCourseResponse.class);
        response.setCategory(mapToCategoryResponse(course.getCategory()));
        response.setModul(course.getChapter().size());
        return response;
    }

    private Course mapToEntityEditCourse(EditCourseRequest editCourseRequest) {
        Course course = new Course();
        course.setTitle(editCourseRequest.getTitle());
        course.setCourseCode(editCourseRequest.getCourseCode());
        course.setCategory(categoryRepository.findByCategoryName(editCourseRequest.getCategory().getCategoryName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "category not found")));
        course.setType(editCourseRequest.getType());
        course.setLevel(editCourseRequest.getLevel());
        course.setPrice(editCourseRequest.getPrice());
        course.setDescription(editCourseRequest.getDescription());
        course.setTelegramLink((editCourseRequest.getTelegramLink()));
        course.setRating(0);

        return course;
    }

    private Course mapToEntityCourse(CourseRequest courseRequest) {
        Course course = modelMapper.map(courseRequest, Course.class);
        course.setCategory(categoryRepository.findByCategoryName(courseRequest.getCategory().getCategoryName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "category not found")));
        course.setRating(0);
        List<Chapter> chapter = courseRequest.getChapter().stream()
                .map(this::mapToEntityChapter)
                .collect(Collectors.toList());

        course.setChapter(chapter);
        return course;
    }
    private Chapter mapToEntityChapter(ChapterRequest chapterRequest) {
        Chapter chapter = modelMapper.map(chapterRequest, Chapter.class);

        List<Subject> subject = chapterRequest.getSubject().stream()
                .map(this::mapToEntitySubject)
                .collect(Collectors.toList());

        chapter.setSubject(subject);

        return chapter;
    }

    private Subject mapToEntitySubject(SubjectRequest subjectRequest) {
        return modelMapper.map(subjectRequest, Subject.class);
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
        DashboardMyCourseResponse response = modelMapper.map(course, DashboardMyCourseResponse.class);
        response.setCategory(mapToCategoryResponse(course.getCategory()));
        response.setPercentProgress(course.getUserProgresses().get(0).getPercent());
        response.setModul(course.getChapter().size());
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
