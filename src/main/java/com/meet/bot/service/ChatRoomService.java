package com.meet.bot.service;

import com.meet.bot.domain.ChatRoom;
import com.meet.bot.domain.User;

import java.util.List;
import java.util.Set;

public interface ChatRoomService {

    List<ChatRoom> findChatRoomsByUser(User user);

    ChatRoom createChatRoom(String name, Set<User> users);

    void addUserToChatRoom(ChatRoom chatRoom, User user);

    void removeUserFromChatRoom(ChatRoom chatRoom, User user);

    ChatRoom createOrGetChatRoom(User user);

}
