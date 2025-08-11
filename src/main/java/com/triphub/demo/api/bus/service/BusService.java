/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 10:39 AM
 */

package com.triphub.demo.api.bus.service;

import com.triphub.demo.api.bus.dto.BusRequestDto;
import com.triphub.demo.config.response.dto.ApiResponse;

public interface BusService {
    ApiResponse retrieveBuses(int page, int limit) throws Exception;
    ApiResponse createBus(BusRequestDto busDto);
    ApiResponse getBusById(Long id);
    ApiResponse updateBus(Long id, BusRequestDto busDto);
    ApiResponse deleteBus(Long id);
}
