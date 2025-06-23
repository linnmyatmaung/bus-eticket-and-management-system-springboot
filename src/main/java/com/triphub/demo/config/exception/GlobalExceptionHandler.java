package com.triphub.demo.config.exception;

import com.triphub.demo.config.response.dto.ApiResponse;
import com.triphub.demo.config.response.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.triphub.demo.config.response.utils.ResponseUtil;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles IllegalArgumentExceptions, typically thrown when method arguments are invalid or inappropriate.
     **/
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseUtil.buildResponse(request,ResponseUtil.error("Invalid arguments provided", HttpStatus.BAD_REQUEST.value(), Map.of("error", ex.getMessage())));
    }


    /**
     * Handles ConstraintViolationException, typically occurring during input validation.
     **/
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        // Build a map of field -> message
        Map<String, String> violations = new HashMap<>();
        for (var violation : ex.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            violations.put(field, message);
        }

        // Wrap violations in meta
        Map<String, Object> meta = Map.of("violations", violations);

        return ResponseUtil.buildResponse(request,ResponseUtil.error( "Validation failed.",
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                meta));

    }

    /**
     * Handles EntityNotFoundException, thrown when an entity cannot be located in the database.
     **/
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        Map<String, Object> meta = Map.of("error", ex.getMessage());
        return ResponseUtil.buildResponse(request,ResponseUtil.error("Entity not found.",
                HttpStatus.NOT_FOUND.value(),
                meta));


    }

    /**
     * Handles DuplicateEntityException, thrown when attempting to create an entity that already exists.
     **/
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ApiResponse> handleDuplicateEntityException(DuplicateEntityException ex, HttpServletRequest request) {
        Map<String, Object> meta = Map.of("error", ex.getMessage());
        return ResponseUtil.buildResponse(request,ResponseUtil.error("Duplicate entity detected.",
                HttpStatus.CONFLICT.value(),
                meta));
    }

    /**
     * Handles BadRequestException, thrown when the client request contains invalid data.
     **/
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return ResponseUtil.buildResponse(request,ResponseUtil.error(  "Bad request.",
                HttpStatus.BAD_REQUEST.value(),
                Map.of("error", ex.getMessage())));
    }

    /**
     * Handles SecurityException, typically thrown when authentication or authorization fails.
     **/
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiResponse> handleSecurityException(SecurityException ex, HttpServletRequest request) {
        return ResponseUtil.buildResponse(request,ResponseUtil.error( "Security violation.",
                HttpStatus.UNAUTHORIZED.value(),
                Map.of("error", ex.getMessage())));
    }

    /**
     * Handles all uncaught exceptions, providing a generic error response.
     **/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        return ResponseUtil.buildResponse(request,ResponseUtil.error("An unexpected error occurred.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Map.of("error", ex.getMessage())));
    }


}
