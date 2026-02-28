package com.sumit.holy_attack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttackResponse {

    private UUID id;
    private String attackerName;
    private String color;
    private String imageUrl;
    private String message;
    private LocalDateTime createdAt;
    private Integer attackCount;
    private UUID referralAttackId;
    private String victimName;
    private String shareableLink;
}