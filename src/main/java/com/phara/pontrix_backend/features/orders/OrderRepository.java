package com.phara.pontrix_backend.features.orders;

import com.phara.pontrix_backend.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.staff.id = :staffId ORDER BY o.id DESC")
    List<Order> findByStaffId(Long staffId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.id DESC")
    List<Order> findByUserId(Long userId);
}

