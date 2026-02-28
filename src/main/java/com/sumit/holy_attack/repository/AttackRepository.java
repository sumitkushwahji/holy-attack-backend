package com.sumit.holy_attack.repository;

import com.sumit.holy_attack.entity.Attack;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttackRepository extends JpaRepository<Attack, UUID> {

    @Query("SELECT a FROM Attack a ORDER BY a.attackCount DESC, a.createdAt DESC")
    List<Attack> findTopAttackersByAttackCount(Pageable pageable);

    @Query("SELECT COUNT(a) FROM Attack a WHERE a.createdAt >= :startOfDay")
    Long countAttacksToday(LocalDateTime startOfDay);

    @Query("SELECT COALESCE(SUM(a.attackCount), 0) FROM Attack a WHERE a.createdAt >= :startOfDay")
    Long sumAttackCountToday(LocalDateTime startOfDay);

    List<Attack> findByAttackerNameContainingIgnoreCase(String attackerName);

    List<Attack> findTop10ByOrderByAttackCountDescCreatedAtDesc();

    List<Attack> findByAttackerNameOrderByCreatedAtDesc(String attackerName);
}