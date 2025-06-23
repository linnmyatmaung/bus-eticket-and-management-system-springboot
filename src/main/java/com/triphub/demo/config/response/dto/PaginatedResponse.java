package com.triphub.demo.config.response.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponse<T> {
    private List<T> items;
    private long totalItems;
    private int lastPage;
}
