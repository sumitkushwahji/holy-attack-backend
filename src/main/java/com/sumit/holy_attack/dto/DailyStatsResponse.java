package com.sumit.holy_attack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatsResponse {

    private Long totalAttacksToday;
    private String formattedMessage;
}