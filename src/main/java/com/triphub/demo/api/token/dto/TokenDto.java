/*
 * @Author : Linn Myat Maung
 * @Date   : 4/13/2025
 * @Time   : 6:05 PM
 */

package com.triphub.demo.api.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private Long id;
    private String refreshToken;
    private Instant expiredAt;
}
