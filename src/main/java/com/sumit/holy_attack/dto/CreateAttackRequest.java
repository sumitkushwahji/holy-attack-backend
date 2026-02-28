package com.sumit.holy_attack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAttackRequest {

    @NotBlank(message = "Attacker name is required")
    @Size(max = 100, message = "Attacker name must not exceed 100 characters")
    private String attackerName;

    @NotBlank(message = "Color is required")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color")
    private String color;

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @Size(max = 200, message = "Message must not exceed 200 characters")
    private String message;

    private UUID referralAttackId;

    @Size(max = 100, message = "Victim name must not exceed 100 characters")
    private String victimName;
}