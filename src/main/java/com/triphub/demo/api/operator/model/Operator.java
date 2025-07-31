/*
 * @Author : Linn Myat Maung
 * @Date   : 7/30/2025
 * @Time   : 10:27 PM
 */

package com.triphub.demo.api.operator.model;


import com.triphub.demo.api.bus.model.Bus;
import com.triphub.demo.api.token.model.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "operator")
@Data   // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Operator{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String operatorName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phNo;


    @Column(nullable = false)
    private int busCount;

    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Bus bus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
