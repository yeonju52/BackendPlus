package com.lion.demo.chatting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired private ChatMessageRepository chatMessageRepository;

    @Override
    public List<ChatMessage> getListByUser(String uid, String friendUid) {
        return List.of();
    }

    @Override
    public ChatMessage getLastChatMessage(String uid, String friendUid) {
        List<ChatMessage> list1 = chatMessageRepository.findBySenderUidAndRecipientUidOrderByTimestampDesc(uid, friendUid);
        List<ChatMessage> list2 = chatMessageRepository.findBySenderUidAndRecipientUidOrderByTimestampDesc(friendUid, uid);
        if (list1.isEmpty() && list2.isEmpty())
            return ChatMessage.builder().message("").timestamp(LocalDateTime.now()).build();
        if (list1.isEmpty())
            return list2.get(0);
        if (list2.isEmpty())
            return list1.get(0);
        return list1.get(0).getTimestamp().isAfter(list2.get(0).getTimestamp()) ? list1.get(0) : list2.get(0);
    }

    @Override
    public int getNewCount(String friendUid, String uid) {
        Integer newCount = chatMessageRepository.getNewCount(friendUid, uid);
        return (newCount != null) ? newCount : 0;
    }

    @Override
    public ChatMessage insertChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}
