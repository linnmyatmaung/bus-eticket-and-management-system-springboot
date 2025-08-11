/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 11:37 AM
 */

package com.triphub.demo.api.operator.service;


import com.triphub.demo.api.operator.dto.OperatorDto;
import com.triphub.demo.config.response.dto.ApiResponse;

public interface OperatorService {
    ApiResponse createOperator(OperatorDto operatorDto);
    ApiResponse getOperatorById(Long id);
    ApiResponse getAllOperators();
    ApiResponse updateOperator(Long id, OperatorDto operatorDto);
    ApiResponse deleteOperator(Long id);
}
