package com.sumit.holy_attack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attacks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attack {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "attacker_name", nullable = false, length = 100)
    private String attackerName;

    @Column(name = "color", nullable = false, length = 10)
    private String color;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "message", length = 200)
    private String message;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "attack_count", nullable = false)
    @Builder.Default
    private Integer attackCount = 1;

    @Column(name = "referral_attack_id")
    private UUID referralAttackId;

    @Column(name = "victim_name", length = 100)
    private String victimName;

    public void incrementAttackCount() {
        this.attackCount += 1;
    }
}