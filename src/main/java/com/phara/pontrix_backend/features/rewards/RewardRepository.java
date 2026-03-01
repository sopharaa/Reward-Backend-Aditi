package com.phara.pontrix_backend.features.rewards;

import com.phara.pontrix_backend.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    // Find all non-deleted rewards
    List<Reward> findByDeletedAtIsNull();

    // Find non-deleted reward by id
    Optional<Reward> findByIdAndDeletedAtIsNull(Long id);

    // Find all rewards by company (non-deleted)
    @Query("SELECT r FROM Reward r WHERE r.company.id = :companyId AND r.deletedAt IS NULL")
    List<Reward> findByCompanyIdAndDeletedAtIsNull(Long companyId);
}

