package com.lion.demo.repository;

import com.lion.demo.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipientRepository extends JpaRepository<Recipient, Long> {
    List<Recipient> findByUserUid(String uid);

    List<Recipient> findByFriendUid(String friendUid);
}