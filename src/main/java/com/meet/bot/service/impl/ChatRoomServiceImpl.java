package com.meet.bot.service.impl;

import com.meet.bot.domain.ChatRoom;
import com.meet.bot.domain.User;
import com.meet.bot.repo.ChatRoomRepository;
import com.meet.bot.service.ChatRoomService;
import com.meet.bot.utils.ChatroomNameGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Override
    public List<ChatRoom> findChatRoomsByUser(User user) {
        return chatRoomRepository.findByUserId(user.getId());
    }

    @Override
    public ChatRoom createChatRoom(String name, Set<User> users) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setUsers(users);
        return chatRoomRepository.save(chatRoom);
    }

    public void addUserToChatRoom(ChatRoom chatRoom, User user) {
        chatRoom.getUsers().add(user);
        chatRoomRepository.save(chatRoom);
    }

    public void removeUserFromChatRoom(ChatRoom chatRoom, User user) {
        chatRoom.getUsers().remove(user);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom createOrGetChatRoom(User user) {
        if (CollectionUtils.isEmpty(user.getChatRooms())) {
            List<ChatRoom> chatRoomsWithUserCountOne = findChatRoomsWithUserCountOne();

            if (CollectionUtils.isEmpty(chatRoomsWithUserCountOne)) {
                return createChatRoom(
                        ChatroomNameGenerator.generateRandomChatroomName(),
                        Set.of(user));
            } else {
                ChatRoom chatRoom = chatRoomsWithUserCountOne.get(0);
                addUserToChatRoom(chatRoom, user);
                return chatRoom;
            }

        } else {
            List<ChatRoom> chatRoomsByUser = findChatRoomsByUser(user);
            return chatRoomsByUser.get(0);
        }
    }



    public List<ChatRoom> findChatRoomsWithUserCountOne() {
        return chatRoomRepository.findChatRoomsWithUserCountOne();
    }

}
