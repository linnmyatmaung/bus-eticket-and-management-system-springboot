/*
 * @Author : Linn Myat Maung
 * @Date   : 8/11/2025
 * @Time   : 9:33 PM
 */

package com.triphub.demo.api.bus.dto;

import com.triphub.demo.api.bus.enums.BusType;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusRequestDto {


    @NotBlank(message = "VIN is required")
    private String vin;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Year is required")
    @Pattern(regexp = "\\d{4}", message = "Year must be a 4-digit number")
    private String year;

    @NotNull(message = "Bus type is required")
    private BusType busType;

    @NotNull(message = "Bus status is required")
    private boolean status;

    @NotNull(message = "Operator ID is required")
    private Long operatorId;


}
