package com.example.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private boolean success;
    private T data;
    private ErrorResponse error;

    // Constructor for success responses
    public BaseResponse(T data) {
        this.success = true;
        this.data = data;
        this.error = null;
    }

    // Constructor for error responses
    public BaseResponse(ErrorResponse error) {
        this.success = false;
        this.data = null;
        this.error = error;
    }

}