package com.triphub.demo.api.Admin.repository;

import com.triphub.demo.api.Admin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminName(String adminName);


    Optional<Admin> findByEmail(String email);
}
