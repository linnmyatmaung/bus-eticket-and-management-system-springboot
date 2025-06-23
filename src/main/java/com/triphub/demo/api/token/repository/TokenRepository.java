/*
 * @Author : Linn Myat Maung
 * @Date   : 4/13/2025
 * @Time   : 6:26 PM
 */

package com.triphub.demo.api.token.repository;



import com.triphub.demo.api.Admin.model.Admin;
import com.triphub.demo.api.token.model.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAdmin(Admin admin);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.admin.id = :adminId")
    void deleteByAdminId(@Param("adminId") Long adminId);



}
