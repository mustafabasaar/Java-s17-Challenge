package com.workintech.s17Challange.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private Integer status;

    private String message;

    private Long timestamp;
}
