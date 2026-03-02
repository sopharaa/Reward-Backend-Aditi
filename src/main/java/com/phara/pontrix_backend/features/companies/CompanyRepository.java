package com.phara.pontrix_backend.features.companies;

import com.phara.pontrix_backend.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL ORDER BY c.id ASC")
    List<Company> findByDeletedAtIsNull();

    Optional<Company> findByIdAndDeletedAtIsNull(Long id);
}

