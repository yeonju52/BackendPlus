package com.lion.demo.chatting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Map<String, List<ChatMessage>> getChatMessagesByDate(String uid, String friendUid) {
        List<ChatMessage> list1 = chatMessageRepository.findBySenderUidAndRecipientUidOrderByTimestampDesc(uid, friendUid);
        List<ChatMessage> list2 = chatMessageRepository.findBySenderUidAndRecipientUidOrderByTimestampDesc(friendUid, uid);
        if (list2 != null) {
            for (ChatMessage cm: list2) {
                if (cm.getHasRead() == 0) {
                    cm.setHasRead(1);           // 화면에 내가 읽었음을 표시
                    chatMessageRepository.updateHasRead(cm.getCmid());      // DB에 내가 읽었음을 표시
                }
            }
        }
        List<ChatMessage> mergedList = Stream.concat(list1.stream(), list2.stream())
                .sorted(Comparator.comparing(ChatMessage::getTimestamp))
                .collect(Collectors.toList());

        Map<String, List<ChatMessage>> map = new LinkedHashMap<>();
        for (ChatMessage cm: mergedList) {
            String date = cm.getTimestamp().toString().substring(0, 10);
            if (map.containsKey(date)) {
                List<ChatMessage> cmList = map.get(date);
                cmList.add(cm);
                map.replace(date, cmList);
            } else {
                List<ChatMessage> cmList = new ArrayList<>();
                cmList.add(cm);
                map.put(date, cmList);
            }
        }
        return map;
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