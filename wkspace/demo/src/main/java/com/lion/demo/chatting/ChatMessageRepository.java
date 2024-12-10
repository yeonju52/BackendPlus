package com.lion.demo.chatting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderUidAndRecipientUid(String uid, String friendUid);

    List<ChatMessage> findBySenderUidAndRecipientUidOrderByTimestampDesc(String uid, String friendUid);

    @Query("select count(c) - sum(c.hasRead) as newCount from ChatMessage c where c.sender.uid=:suid and c.recipient.uid=:ruid")
    Integer getNewCount(@Param("suid") String suid, @Param("ruid") String ruid);

    @Transactional
    @Modifying
    @Query("update ChatMessage c set c.hasRead=1 where c.cmid=:cmid")
    void updateHasRead(long cmid);
}
