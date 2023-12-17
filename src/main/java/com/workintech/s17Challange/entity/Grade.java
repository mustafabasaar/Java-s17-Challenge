package com.workintech.s17Challange.entity;

import lombok.Data;

@Data
public class Grade {
    Integer coefficient;
    String note;

    public Grade(Integer coefficient, String note) {
        this.coefficient = coefficient;
        this.note = note;
    }

}