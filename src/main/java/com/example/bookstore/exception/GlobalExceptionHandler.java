package com.example.bookstore.exception;

import com.example.bookstore.dto.BaseResponse;
import com.example.bookstore.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Handle validation errors (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), "Validation failed: " + error.getDefaultMessage()));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation errors occurred");
        BaseResponse<Map<String, String>> response = new BaseResponse<>(errorResponse);
        response.setData(errors); // Correctly setting the field-specific errors in data
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<BaseResponse<String>> handleUsernameNotFoundException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Authentication failed: " + ex.getMessage()
        );
        BaseResponse<String> response = new BaseResponse<>(errorResponse);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle resource not found (404 Not Found)
    @ExceptionHandler({ResourceNotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<BaseResponse<Map<String, String>>> handleNotFoundExceptions(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Resource not found: " + ex.getMessage());
        BaseResponse<Map<String, String>> response = new BaseResponse<>(errorResponse);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle bad requests (400 Bad Request)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<String>> handleBadRequest(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad request: " + ex.getMessage());
        BaseResponse<String> response = new BaseResponse<>(errorResponse);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle unauthorized access (401 Unauthorized)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<BaseResponse<String>> handleUnauthorized(UnauthorizedAccessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: " + ex.getMessage());
        BaseResponse<String> response = new BaseResponse<>(errorResponse);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Resource not found: " + ex.getRequestURL()
        );
        BaseResponse<String> response = new BaseResponse<>(errorResponse);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<BaseResponse<String>> handleConflict(ConflictException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflict: " + ex.getMessage());
        BaseResponse<String> response = new BaseResponse<>(errorResponse);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handle runtime exceptions (500 Internal Server Error)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<String>> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + ex.getMessage());
        BaseResponse<String> response = new BaseResponse<>(errorResponse);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle all other exceptions (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage());
        BaseResponse<String> response = new BaseResponse<>(errorResponse);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}