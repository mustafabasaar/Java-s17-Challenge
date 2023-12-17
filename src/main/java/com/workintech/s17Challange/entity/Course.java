package com.workintech.s17Challange.entity;

import lombok.Data;

@Data
public class Course {
    private Integer id;
    String name;
    Integer credit;
    Grade grade;
}
