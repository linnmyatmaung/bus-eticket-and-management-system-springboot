/*
 * @Author : Linn Myat Maung
 * @Date   : 8/10/2025
 * @Time   : 10:33 AM
 */

package com.triphub.demo.api.operator.repository;


import com.triphub.demo.api.operator.model.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
    Optional<Operator> findByOperatorName(String operatorName);
    boolean existsByOperatorName(String operatorName);
}
