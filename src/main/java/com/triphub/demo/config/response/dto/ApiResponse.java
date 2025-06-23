package com.triphub.demo.config.response.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ApiResponse {
    private boolean success;
    private int code;
    private Map<String, Object> meta;
    private Object data;
    private String message;
    private double duration;
}