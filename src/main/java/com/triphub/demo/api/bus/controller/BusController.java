/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 8:53 PM
 */

package com.triphub.demo.api.bus.controller;

import com.triphub.demo.api.bus.dto.BusDto;
import com.triphub.demo.api.bus.service.BusService;
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
@RequestMapping("/${api.base.path}/buses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bus", description = "Bus management endpoints")
public class BusController {

    private final BusService busService;

    @PostMapping
    @Operation(summary = "Create Bus", description = "Add a new bus")
    public ResponseEntity<ApiResponse> createBus(@Validated @RequestBody BusDto busDto, HttpServletRequest request) {
        ApiResponse response = busService.createBus(busDto);
        return ResponseUtil.buildResponse(request, response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Bus by ID", description = "Retrieve bus details by ID")
    public ResponseEntity<ApiResponse> getBusById(@PathVariable Long id, HttpServletRequest request) {
        ApiResponse response = busService.getBusById(id);
        return ResponseUtil.buildResponse(request, response);
    }

    @GetMapping
    @Operation(summary = "Get All Buses (Paginated)", description = "List all buses with pagination")
    public ResponseEntity<ApiResponse> getAllBuses(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) throws Exception {
        log.info("Retrieving buses - Page: {}, Limit: {}", page, limit);

        ApiResponse response = busService.retrieveBuses(page, limit);
        return ResponseUtil.buildResponse(request, response);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update Bus", description = "Update an existing bus")
    public ResponseEntity<ApiResponse> updateBus(@PathVariable Long id, @Validated @RequestBody BusDto busDto, HttpServletRequest request) {
        ApiResponse response = busService.updateBus(id, busDto);
        return ResponseUtil.buildResponse(request, response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Bus", description = "Delete bus by ID")
    public ResponseEntity<ApiResponse> deleteBus(@PathVariable Long id, HttpServletRequest request) {
        ApiResponse response = busService.deleteBus(id);
        return ResponseUtil.buildResponse(request, response);
    }
}
