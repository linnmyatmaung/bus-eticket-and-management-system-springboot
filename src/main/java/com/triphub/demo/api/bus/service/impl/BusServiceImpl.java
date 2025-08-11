/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 10:43 AM
 */
package com.triphub.demo.api.bus.service.impl;

import com.triphub.demo.api.bus.dto.BusDto;
import com.triphub.demo.api.bus.model.Bus;
import com.triphub.demo.api.bus.repository.BusRepository;
import com.triphub.demo.api.operator.model.Operator;
import com.triphub.demo.api.operator.repository.OperatorRepository;
import com.triphub.demo.api.bus.service.BusService;
import com.triphub.demo.config.response.dto.ApiResponse;
import com.triphub.demo.config.response.dto.PaginatedResponse;
import com.triphub.demo.config.response.utils.ResponseUtil;
import com.triphub.demo.config.utils.EntityUtil;
import com.triphub.demo.config.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final OperatorRepository operatorRepository;
    private final MapperUtil mapperUtil;

    @Override
    public ApiResponse createBus(BusDto busDto) {
        if (busRepository.existsByVin(busDto.getVin())) {
            return ResponseUtil.error("VIN already exists", HttpStatus.CONFLICT.value(), null);
        }

        Operator operator = EntityUtil.getEntityById(operatorRepository, busDto.getOperatorId(), "Operator");

        Bus bus = mapperUtil.convertToEntity(busDto, Bus.class);
        bus.setOperator(operator);

        EntityUtil.saveEntityWithoutReturn(busRepository, bus, "Bus");
        log.info("Created new bus with VIN: {}", bus.getVin());

        return ResponseUtil.success("Bus created successfully", HttpStatus.CREATED.value(),
                mapperUtil.convertToDto(bus, BusDto.class));
    }

    @Override
    public ApiResponse getBusById(Long id) {
        Bus bus = EntityUtil.getEntityById(busRepository, id, "Bus");
        return ResponseUtil.success("Bus fetched successfully", HttpStatus.OK.value(),
                mapperUtil.convertToDto(bus, BusDto.class));
    }



    @Override
    public ApiResponse retrieveBuses(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("id").ascending());
        Page<Bus> busPage = busRepository.findAll(pageable);

        List<BusDto> busDtos = busPage.getContent().stream()
                .map(bus -> {
                    BusDto dto = mapperUtil.convertToDto(bus, BusDto.class);
                    if (bus.getOperator() != null) {
                        dto.setOperatorName(bus.getOperator().getOperatorName());
                        dto.setOperatorImage(bus.getOperator().getOperatorImage());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        PaginatedResponse<BusDto> paginatedResponse = PaginatedResponse.<BusDto>builder()
                .items(busDtos)
                .totalItems(busPage.getTotalElements())
                .lastPage(busPage.getTotalPages())
                .build();

        return ResponseUtil.success("Buses fetched successfully", HttpStatus.OK.value(), paginatedResponse);
    }



    @Override
    public ApiResponse updateBus(Long id, BusDto busDto) {
        Bus existingBus = EntityUtil.getEntityById(busRepository, id, "Bus");

        if (!existingBus.getVin().equals(busDto.getVin()) && busRepository.existsByVin(busDto.getVin())) {
            return ResponseUtil.error("VIN already exists", HttpStatus.CONFLICT.value(), null);
        }

        Operator operator = EntityUtil.getEntityById(operatorRepository, busDto.getOperatorId(), "Operator");

        mapperUtil.updateEntity(busDto, existingBus);
        existingBus.setOperator(operator);

        EntityUtil.saveEntityWithoutReturn(busRepository, existingBus, "Bus");
        log.info("Updated bus with ID: {}", id);

        return ResponseUtil.success("Bus updated successfully", HttpStatus.OK.value(),
                mapperUtil.convertToDto(existingBus, BusDto.class));
    }

    @Override
    public ApiResponse deleteBus(Long id) {
        EntityUtil.deleteEntity(busRepository, id, "Bus");
        log.info("Deleted bus with ID: {}", id);
        return ResponseUtil.success("Bus deleted successfully", HttpStatus.OK.value(), true);
    }
}
