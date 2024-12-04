package com.lion.demo.repository;

import com.lion.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 개별 사용자
    List<Order> findByUserUid(String uid);

    // 관리자 - 기간 설정
    List<Order> findByOrderDateTimeBetween(LocalDateTime start, LocalDateTime end);
}