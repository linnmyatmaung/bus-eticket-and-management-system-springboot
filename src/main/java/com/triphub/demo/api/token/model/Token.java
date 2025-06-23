/*
 * @Author : Linn Myat Maung
 * @Date   : 4/13/2025
 * @Time   : 6:23 PM
 */

package com.triphub.demo.api.token.model;


import com.triphub.demo.api.Admin.model.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String refreshToken;

    @Column(nullable = false)
    private Instant expiredAt;

    @OneToOne
    @JoinColumn(name = "admin_id", nullable = false, unique = true)
    private Admin admin;
}
