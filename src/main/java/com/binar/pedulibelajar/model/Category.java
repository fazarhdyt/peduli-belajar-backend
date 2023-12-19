package com.binar.pedulibelajar.model;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CourseCategory categoryName;

    private String categoryImage;

    @OneToMany(mappedBy = "category")
    private List<Course> courses;
}
