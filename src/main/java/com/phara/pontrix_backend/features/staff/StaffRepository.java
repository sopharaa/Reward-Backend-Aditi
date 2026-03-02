package com.phara.pontrix_backend.features.staff;

import com.phara.pontrix_backend.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByDeletedAtIsNull();
    Optional<Staff> findByIdAndDeletedAtIsNull(Long id);
    List<Staff> findByCompanyIdAndDeletedAtIsNull(Long companyId);
    boolean existsByEmail(String email);
    Optional<Staff> findByEmail(String email);
}

