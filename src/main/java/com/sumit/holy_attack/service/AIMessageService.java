package com.sumit.holy_attack.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class AIMessageService {

    private final Random random = new Random();
    
    private final List<String> fallbackMessages = List.of(
        "💥 You just got SPLASHED! No cap! 🎨",
        "🌈 Color attack incoming! You're officially PAINTED! ✨",
        "💫 SPLASHHHHH! Hope you're ready for some rainbow vibes! 🎭",
        "🎨 Surprise! You've been colorfully attacked! So aesthetic! ✨",
        "💥 BAM! Color bomb delivered! You look fabulous! 🌈",
        "🎪 GOTCHA! Time for some Holi magic, bestie! 💫",
        "🌟 SPLASH ATTACK! Adding some color to your day! 🎨",
        "💖 You've been blessed with rainbow energy! So pretty! ✨",
        "🎭 COLOR STORM incoming! Prepare for maximum vibes! 🌈",
        "💥 BOOM! Holi celebration mode: ACTIVATED! 🎉"
    );

    public String generateAttackMessage(String attackerName, String color) {
        log.info("Generating AI message for attacker: {} with color: {}", attackerName, color);
        
        try {
            // TODO: Integrate with OpenAI API for custom message generation
            // For now, using fallback messages
            String message = generateFallbackMessage(attackerName, color);
            log.info("Generated message: {}", message);
            return message;
            
        } catch (Exception e) {
            log.warn("Failed to generate AI message, using fallback: {}", e.getMessage());
            return getFallbackMessage();
        }
    }

    private String generateFallbackMessage(String attackerName, String color) {
        String colorEmoji = getColorEmoji(color);
        String baseMessage = fallbackMessages.get(random.nextInt(fallbackMessages.size()));
        
        return String.format("%s %s sent this! %s", 
            attackerName, 
            baseMessage, 
            colorEmoji
        );
    }

    private String getFallbackMessage() {
        return fallbackMessages.get(random.nextInt(fallbackMessages.size()));
    }

    private String getColorEmoji(String color) {
        return switch (color.toLowerCase()) {
            case "#ff69b4", "#ff1493", "#da70d6" -> "💖";  // Pink
            case "#ffd700", "#ffff00" -> "⭐";              // Yellow
            case "#87ceeb", "#0000ff", "#00bfff" -> "💙";   // Blue
            case "#ffa500", "#ff8c00" -> "🧡";              // Orange
            case "#32cd32", "#00ff00", "#228b22" -> "💚";   // Green
            case "#9370db", "#8a2be2", "#9400d3" -> "💜";   // Purple
            default -> "🌈";
        };
    }
}