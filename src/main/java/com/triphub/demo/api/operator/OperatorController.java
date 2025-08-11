/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 11:46 AM
 */

package com.triphub.demo.api.operator.controller;

import com.triphub.demo.api.operator.dto.OperatorRequestDto;
import com.triphub.demo.api.operator.service.OperatorService;
import com.triphub.demo.config.response.dto.ApiResponse;
import com.triphub.demo.config.response.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.base.path}/operators")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Operator", description = "Operator management endpoints")
public class OperatorController {

    private final OperatorService operatorService;

    @PostMapping
    @Operation(summary = "Create Operator", description = "Add a new operator")
    public ResponseEntity<ApiResponse> createOperator(@Validated @RequestBody OperatorRequestDto operatorDto,
                                                      HttpServletRequest request) {
        ApiResponse response = operatorService.createOperator(operatorDto);
        return ResponseUtil.buildResponse(request, response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Operator by ID", description = "Retrieve operator details by ID")
    public ResponseEntity<ApiResponse> getOperatorById(@PathVariable Long id, HttpServletRequest request) {
        ApiResponse response = operatorService.getOperatorById(id);
        return ResponseUtil.buildResponse(request, response);
    }

    @GetMapping
    @Operation(summary = "Get All Operators", description = "List all operators")
    public ResponseEntity<ApiResponse> getAllOperators(HttpServletRequest request) {
        ApiResponse response = operatorService.getAllOperators();
        return ResponseUtil.buildResponse(request, response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Operator", description = "Update an existing operator")
    public ResponseEntity<ApiResponse> updateOperator(@PathVariable Long id,
                                                      @Validated @RequestBody OperatorRequestDto operatorDto,
                                                      HttpServletRequest request) {
        ApiResponse response = operatorService.updateOperator(id, operatorDto);
        return ResponseUtil.buildResponse(request, response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Operator", description = "Delete operator by ID")
    public ResponseEntity<ApiResponse> deleteOperator(@PathVariable Long id, HttpServletRequest request) {
        ApiResponse response = operatorService.deleteOperator(id);
        return ResponseUtil.buildResponse(request, response);
    }
}
