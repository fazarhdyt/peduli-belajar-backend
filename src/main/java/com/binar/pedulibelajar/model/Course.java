package com.binar.pedulibelajar.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    private int id;
    private User user;
    private String category;
    private String type;
    private String level;
    private double price;
    private String description;
    private String video;



}
