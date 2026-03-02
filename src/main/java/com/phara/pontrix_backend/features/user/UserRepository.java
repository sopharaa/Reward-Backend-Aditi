package com.phara.pontrix_backend.features.user;

import com.phara.pontrix_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL ORDER BY u.id ASC")
    List<User> findByDeletedAtIsNull();

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT u FROM User u WHERE u.company.id = :companyId AND u.deletedAt IS NULL ORDER BY u.id ASC")
    List<User> findByCompanyIdAndDeletedAtIsNull(Long companyId);

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
