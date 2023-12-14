package com.binar.pedulibelajar.model;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Course {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String title;

    @Column(unique = true)
    private String courseCode;

    @Enumerated(EnumType.STRING)
    private CourseCategory category;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    private double price;

    @Column(columnDefinition = "text")
    private String description;

    private String teacher;

    private double rating;

    private String thumbnail;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapter;

    @OneToMany(mappedBy = "course")
    private List<Order> order;

    @OneToMany(mappedBy = "course")
    private List<UserCourse> userCourses;

}
