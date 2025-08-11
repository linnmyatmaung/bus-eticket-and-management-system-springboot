/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 11:42 AM
 */

package com.triphub.demo.api.operator.service.impl;

import com.triphub.demo.api.operator.dto.OperatorDto;
import com.triphub.demo.api.operator.model.Operator;
import com.triphub.demo.api.operator.repository.OperatorRepository;
import com.triphub.demo.api.operator.service.OperatorService;
import com.triphub.demo.config.response.dto.ApiResponse;
import com.triphub.demo.config.response.utils.ResponseUtil;
import com.triphub.demo.config.utils.EntityUtil;
import com.triphub.demo.config.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperatorServiceImpl implements OperatorService {

    private final OperatorRepository operatorRepository;
    private final MapperUtil mapperUtil;

    @Override
    public ApiResponse createOperator(OperatorDto operatorDto) {
        if (operatorRepository.existsByOperatorName(operatorDto.getOperatorName())) {
            return ResponseUtil.error("Operator name already exists", HttpStatus.CONFLICT.value(), null);
        }

        Operator operator = mapperUtil.convertToEntity(operatorDto, Operator.class);
        EntityUtil.saveEntityWithoutReturn(operatorRepository, operator, "Operator");

        log.info("Created new operator: {}", operator.getOperatorName());
        return ResponseUtil.success("Operator created successfully", HttpStatus.CREATED.value(),
                mapperUtil.convertToDto(operator, OperatorDto.class));
    }

    @Override
    public ApiResponse getOperatorById(Long id) {
        Operator operator = EntityUtil.getEntityById(operatorRepository, id, "Operator");
        return ResponseUtil.success("Operator fetched successfully", HttpStatus.OK.value(),
                mapperUtil.convertToDto(operator, OperatorDto.class));
    }

    @Override
    public ApiResponse getAllOperators() {
        List<Operator> operators = EntityUtil.getAllEntities(operatorRepository, Sort.by(Sort.Direction.ASC, "id"), "Operator");
        List<OperatorDto> dtoList = mapperUtil.convertToDtoList(operators, OperatorDto.class);
        return ResponseUtil.success("Operators fetched successfully", HttpStatus.OK.value(), dtoList);
    }

    @Override
    public ApiResponse updateOperator(Long id, OperatorDto operatorDto) {
        Operator existingOperator = EntityUtil.getEntityById(operatorRepository, id, "Operator");

        if (!existingOperator.getOperatorName().equals(operatorDto.getOperatorName()) &&
                operatorRepository.existsByOperatorName(operatorDto.getOperatorName())) {
            return ResponseUtil.error("Operator name already exists", HttpStatus.CONFLICT.value(), null);
        }

        mapperUtil.updateEntity(operatorDto, existingOperator);

        EntityUtil.saveEntityWithoutReturn(operatorRepository, existingOperator, "Operator");
        log.info("Updated operator with ID: {}", id);

        return ResponseUtil.success("Operator updated successfully", HttpStatus.OK.value(),
                mapperUtil.convertToDto(existingOperator, OperatorDto.class));
    }

    @Override
    public ApiResponse deleteOperator(Long id) {
        EntityUtil.deleteEntity(operatorRepository, id, "Operator");
        log.info("Deleted operator with ID: {}", id);
        return ResponseUtil.success("Operator deleted successfully", HttpStatus.OK.value(), true);
    }
}
