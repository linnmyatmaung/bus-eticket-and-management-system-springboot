/*
 * @Author : Linn Myat Maung
 * @Date   : 8/11/2025
 * @Time   : 9:46 PM
 */

/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 11:34 AM
 */

package com.triphub.demo.api.operator.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperatorRequestDto {


    @NotBlank(message = "Operator name is required")
    private String operatorName;

    @NotBlank(message = "Operator name is required")
    private String operatorImage;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phNo;

    @Min(value = 0, message = "Bus count cannot be negative")
    private int busCount;
}
