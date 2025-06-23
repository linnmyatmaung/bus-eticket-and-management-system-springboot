package com.triphub.demo.api.Admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private Long id;
    private String adminName;
    private String email;
    private String createdAt;
    private String updatedAt;
}