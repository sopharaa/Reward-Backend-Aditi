package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("SELECT s FROM Staff s WHERE s.deletedAt IS NULL ORDER BY s.id ASC")
    List<Staff> findByDeletedAtIsNull();

    Optional<Staff> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT s FROM Staff s WHERE s.company.id = :companyId AND s.deletedAt IS NULL ORDER BY s.id ASC")
    List<Staff> findByCompanyIdAndDeletedAtIsNull(Long companyId);

    boolean existsByEmail(String email);
    Optional<Staff> findByEmail(String email);
}

