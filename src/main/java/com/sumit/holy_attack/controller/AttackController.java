package com.sumit.holy_attack.controller;

import com.sumit.holy_attack.dto.*;
import com.sumit.holy_attack.service.AttackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/attacks")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AttackController {

    private final AttackService attackService;

    @PostMapping
    public ResponseEntity<CreateAttackResponse> createAttack(@Valid @RequestBody CreateAttackRequest request) {
        log.info("API: Creating attack for {}", request.getAttackerName());
        try {
            CreateAttackResponse response = attackService.createAttack(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Failed to create attack", e);
            throw new RuntimeException("Failed to create attack: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttackResponse> getAttack(@PathVariable UUID id) {
        log.info("API: Getting attack with id {}", id);
        try {
            Optional<AttackResponse> response = attackService.getAttackById(id);
            return response.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error getting attack: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/splash")
    public ResponseEntity<AttackResponse> incrementAttackCounter(@PathVariable UUID id) {
        log.info("API: Incrementing counter for attack {}", id);
        try {
            Optional<AttackResponse> response = attackService.incrementAttackCounter(id);
            return response.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error incrementing attack counter: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(
            @RequestParam(defaultValue = "10") int limit) {
        log.info("API: Getting leaderboard with limit {}", limit);
        try {
            List<LeaderboardEntry> leaderboard = attackService.getLeaderboard(limit);
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            log.error("Failed to get leaderboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/daily")
    public ResponseEntity<DailyStatsResponse> getDailyStats() {
        log.info("API: Getting daily stats");
        try {
            DailyStatsResponse stats = attackService.getDailyStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Failed to get daily stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<List<AttackResponse>> getMyAttacks(@RequestParam String attackerName) {
        log.info("API: Getting attacks for {}", attackerName);
        try {
            List<AttackResponse> attacks = attackService.getAttacksByAttacker(attackerName);
            return ResponseEntity.ok(attacks);
        } catch (Exception e) {
            log.error("Failed to get attacks for user: {}", attackerName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Holy Attack API is running! 🎨");
    }
}