package com.binar.pedulibelajar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Course {

    @Id
    private String id;

    private String name;
    private String courseCode;
    private String category;
    private String type;
    private String level;
    private long price;
    private String description;
    private Set<String> video;
}
