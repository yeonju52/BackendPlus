package com.lion.demo.chatting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipientRepository extends JpaRepository<Recipient, Long> {
    List<Recipient> findByUserUid(String uid);

    List<Recipient> findByFriendUid(String friendUid);
}
