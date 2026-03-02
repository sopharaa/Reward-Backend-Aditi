package com.phara.pontrix_backend.features.rewards;

import com.phara.pontrix_backend.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    @Query("SELECT r FROM Reward r WHERE r.deletedAt IS NULL ORDER BY r.id ASC")
    List<Reward> findByDeletedAtIsNull();

    Optional<Reward> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT r FROM Reward r WHERE r.company.id = :companyId AND r.deletedAt IS NULL ORDER BY r.id ASC")
    List<Reward> findByCompanyIdAndDeletedAtIsNull(Long companyId);
}

