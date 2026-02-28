package com.sumit.holy_attack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttackResponse {

    private UUID attackId;
    private String shareableLink;
    private String message;
}