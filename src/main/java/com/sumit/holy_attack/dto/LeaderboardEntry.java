package com.sumit.holy_attack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntry {

    private String attackerName;
    private Integer totalAttacks;
    private LocalDateTime lastAttack;
    private Integer rank;
}