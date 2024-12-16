package com.lion.demo.service;

import com.lion.demo.entity.Recipient;
import com.lion.demo.entity.User;

import java.util.List;

public interface RecipientService {
    List<Recipient> getFriendList(String uid);

    void insertFriend(User user, User friend);
}