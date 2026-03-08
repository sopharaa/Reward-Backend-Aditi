package com.phara.pontrix_backend.features.redemptions;

import com.phara.pontrix_backend.domain.Redeem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedeemRepository extends JpaRepository<Redeem, Long> {

    @Query("SELECT r FROM Redeem r WHERE r.user.id = :userId AND r.deletedAt IS NULL ORDER BY r.createdAt DESC")
    List<Redeem> findByUserIdAndDeletedAtIsNull(Long userId);

    @Query("SELECT r FROM Redeem r WHERE r.status = :status AND r.deletedAt IS NULL AND r.user.company.id = :companyId ORDER BY r.createdAt DESC")
    List<Redeem> findByStatusAndCompanyIdAndDeletedAtIsNull(String status, Long companyId);

    @Query("SELECT r FROM Redeem r WHERE r.deletedAt IS NULL AND r.user.company.id = :companyId ORDER BY r.createdAt DESC")
    List<Redeem> findByCompanyIdAndDeletedAtIsNull(Long companyId);

    Optional<Redeem> findByIdAndDeletedAtIsNull(Long id);
}

