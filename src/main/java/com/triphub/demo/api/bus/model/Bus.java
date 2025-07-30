/*
 * @Author : Linn Myat Maung
 * @Date   : 7/28/2025
 * @Time   : 8:34 PM
 */

package com.triphub.demo.api.bus.model;

import com.triphub.demo.api.token.model.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus")
@Data   // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Bus{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
}