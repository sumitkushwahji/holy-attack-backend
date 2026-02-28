package com.sumit.holy_attack.service;

import com.sumit.holy_attack.dto.*;
import com.sumit.holy_attack.entity.Attack;
import com.sumit.holy_attack.repository.AttackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttackService {

    private final AttackRepository attackRepository;
    private final AIMessageService aiMessageService;

    @Value("${app.base-url:http://localhost:4200}")
    private String baseUrl;

    @Transactional
    @CacheEvict(value = {"leaderboard", "dailyStats"}, allEntries = true)
    public CreateAttackResponse createAttack(CreateAttackRequest request) {
        log.info("Creating attack for attacker: {}", request.getAttackerName());

        String message = request.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = aiMessageService.generateAttackMessage(
                request.getAttackerName(), 
                request.getColor()
            );
        }

        Attack attack = Attack.builder()
                .attackerName(request.getAttackerName())
                .color(request.getColor())
                .imageUrl(request.getImageUrl())
                .message(message)
                .referralAttackId(request.getReferralAttackId())
                .victimName(request.getVictimName())
                .attackCount(1)
                .build();

        Attack savedAttack = attackRepository.save(attack);

        String shareableLink = baseUrl + "/attack/" + savedAttack.getId();

        log.info("Attack created successfully with ID: {}", savedAttack.getId());

        return CreateAttackResponse.builder()
                .attackId(savedAttack.getId())
                .shareableLink(shareableLink)
                .message(message)
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<AttackResponse> getAttackById(UUID attackId) {
        log.info("Fetching attack with ID: {}", attackId);

        return attackRepository.findById(attackId)
                .map(this::convertToAttackResponse);
    }

    @Transactional
    @CacheEvict(value = {"leaderboard", "dailyStats"}, allEntries = true)
    public Optional<AttackResponse> incrementAttackCounter(UUID attackId) {
        log.info("Incrementing attack counter for ID: {}", attackId);

        Optional<Attack> attackOpt = attackRepository.findById(attackId);
        if (attackOpt.isPresent()) {
            Attack attack = attackOpt.get();
            attack.incrementAttackCount();
            Attack savedAttack = attackRepository.save(attack);
            
            log.info("Attack counter incremented to {} for ID: {}", savedAttack.getAttackCount(), attackId);
            return Optional.of(convertToAttackResponse(savedAttack));
        }
        
        log.warn("Attack not found for ID: {}", attackId);
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Cacheable("leaderboard")
    public List<LeaderboardEntry> getLeaderboard(int limit) {
        log.info("Fetching leaderboard data with limit: {}", limit);

        PageRequest pageRequest = PageRequest.of(0, limit);
        List<Attack> topAttacks = attackRepository.findTopAttackersByAttackCount(pageRequest);
        
        AtomicInteger rank = new AtomicInteger(1);
        return topAttacks.stream()
                .map(attack -> LeaderboardEntry.builder()
                        .attackerName(attack.getAttackerName())
                        .totalAttacks(attack.getAttackCount())
                        .lastAttack(attack.getCreatedAt())
                        .rank(rank.getAndIncrement())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable("dailyStats")
    public DailyStatsResponse getDailyStats() {
        log.info("Fetching daily stats");

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        Long totalAttacks = attackRepository.sumAttackCountToday(startOfDay);
        
        if (totalAttacks == null) {
            totalAttacks = 0L;
        }

        String formattedMessage = String.format("%,d people attacked today 🌈", totalAttacks);

        return DailyStatsResponse.builder()
                .totalAttacksToday(totalAttacks)
                .formattedMessage(formattedMessage)
                .build();
    }

    @Transactional(readOnly = true)
    public List<AttackResponse> getAttacksByAttacker(String attackerName) {
        log.info("Fetching attacks for attacker: {}", attackerName);
        
        List<Attack> attacks = attackRepository.findByAttackerNameOrderByCreatedAtDesc(attackerName);
        return attacks.stream()
                .map(this::convertToAttackResponse)
                .toList();
    }

    private AttackResponse convertToAttackResponse(Attack attack) {
        String shareableLink = baseUrl + "/attack/" + attack.getId();

        return AttackResponse.builder()
                .id(attack.getId())
                .attackerName(attack.getAttackerName())
                .color(attack.getColor())
                .imageUrl(attack.getImageUrl())
                .message(attack.getMessage())
                .createdAt(attack.getCreatedAt())
                .attackCount(attack.getAttackCount())
                .referralAttackId(attack.getReferralAttackId())
                .victimName(attack.getVictimName())
                .shareableLink(shareableLink)
                .build();
    }
}