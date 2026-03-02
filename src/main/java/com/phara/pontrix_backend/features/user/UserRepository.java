package com.phara.pontrix_backend.features.user;

import com.phara.pontrix_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByDeletedAtIsNull();
    Optional<User> findByIdAndDeletedAtIsNull(Long id);
    List<User> findByCompanyIdAndDeletedAtIsNull(Long companyId);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
