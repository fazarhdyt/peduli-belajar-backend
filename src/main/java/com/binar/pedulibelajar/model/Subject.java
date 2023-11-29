package com.binar.pedulibelajar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Subject {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private int subjectNo;
    private String videoTitle;
    private String videoLink;
    private String description;

    @ManyToOne
    @JoinColumn(name = "chapter_id", referencedColumnName = "id")
    private Chapter chapter;

    @ManyToOne
    @JoinColumn(name = "material_type_id", referencedColumnName = "id")
    private MaterialType materialType;

}
