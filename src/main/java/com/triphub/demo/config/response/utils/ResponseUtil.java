package com.triphub.demo.config.response.utils;

import com.triphub.demo.config.response.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class ResponseUtil {


    private static long calculateDuration(HttpServletRequest request) {
        Long startTime = (Long) request.getAttribute("startTime");
        return (startTime != null) ? System.currentTimeMillis() - startTime : 0;
    }


    // Build ResponseEntity from an ApiResponse (existing method)
    public static ResponseEntity<ApiResponse> buildResponse(HttpServletRequest request, ApiResponse response) {
        HttpStatus status = HttpStatus.valueOf(response.getCode());


        if (response.getMeta() == null) {
            String method = request.getMethod();
            String endpoint = request.getRequestURI();
            response.setMeta(new HashMap<>());
            response.getMeta().put("method", method);
            response.getMeta().put("endpoint", endpoint);
        }

        response.setDuration(calculateDuration(request));
        return new ResponseEntity<>(response, status);
    }

    // ✅ Helper for successful response
    public static ApiResponse success(String message, int code, Object data) {

        return ApiResponse.builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    // ✅ Helper for error response
    public static ApiResponse error(String message, int code, Object data) {

        return ApiResponse.builder()
                .success(false)
                .code(code)
                .message(message)

                .build();

    }



}
