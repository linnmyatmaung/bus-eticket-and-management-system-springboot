/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 10:33 AM
 */

package com.triphub.demo.api.bus.repository;

import com.triphub.demo.api.bus.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    Optional<Bus> findByVin(String vin);
    boolean existsByVin(String vin);
}
