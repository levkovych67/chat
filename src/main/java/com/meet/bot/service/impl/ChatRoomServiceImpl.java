package com.meet.bot.service.impl;

import com.meet.bot.domain.ChatRoom;
import com.meet.bot.domain.User;
import com.meet.bot.repo.ChatRoomRepository;
import com.meet.bot.service.ChatRoomService;
import com.meet.bot.utils.ChatroomNameGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Override
    public List<ChatRoom> findChatRoomsByUser(User user) {
        return chatRoomRepository.findByUserId(user.getId());
    }

    @Override
    public ChatRoom createOrFindChatRoomForUser(String name, Set<User> users) {
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

    public void deleteChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
    }

    @Override
    public ChatRoom createOrGetChatRoom(User user) {
        if (CollectionUtils.isEmpty(user.getChatRooms())) {
            List<ChatRoom> chatRoomsWithUserCountOne = findChatRoomsWithUserCountOne();
            if (CollectionUtils.isEmpty(chatRoomsWithUserCountOne)) {
                return createOrFindChatRoomForUser(
                        ChatroomNameGenerator.generateRandomChatroomName(),
                        Set.of(user));
            } else {
                Collections.shuffle(chatRoomsWithUserCountOne);
                ChatRoom chatRoom = chatRoomsWithUserCountOne.get(0);
                addUserToChatRoom(chatRoom, user);
                return chatRoom;
            }

        } else {
            List<ChatRoom> chatRoomsByUser = findChatRoomsByUser(user);
            return chatRoomsByUser.get(0);
        }
    }

    public Optional<ChatRoom> findChatRoomByUser(User user) {
        List<ChatRoom> chatRoomsByUser = findChatRoomsByUser(user);
        if (CollectionUtils.isEmpty(chatRoomsByUser)) {
            return Optional.empty();
        } else {
            if (chatRoomsByUser.size() > 1) {
                throw new RuntimeException("User " + user.getTelegramId() + " has more than 1 room");
            }
            return Optional.of(chatRoomsByUser.get(0));
        }
    }

    public ChatRoom createOrFindChatRoomForUser(User user) {
        List<ChatRoom> chatRoomsWithUserCountOne = findChatRoomsWithUserCountOne();

        if (CollectionUtils.isEmpty(chatRoomsWithUserCountOne)) {
            return createOrFindChatRoomForUser(
                    ChatroomNameGenerator.generateRandomChatroomName(),
                    Set.of(user));
        } else {
            Collections.shuffle(chatRoomsWithUserCountOne);
            ChatRoom chatRoom = chatRoomsWithUserCountOne.get(0);
            addUserToChatRoom(chatRoom, user);
            return chatRoom;
        }
    }

    @Override
    public void deleteChatRooms(Set<ChatRoom> rooms) {
//        for (ChatRoom chatRoom :rooms) {
//            Set<User> users = chatRoom.getUsers();
//            for (User user : users) {
//                removeUserFromChatRoom(chatRoom,user);
//            }
//        }
        for (ChatRoom chatRoom : rooms) {
            deleteChatRoom(chatRoom);
        }
    }


    public List<ChatRoom> findChatRoomsWithUserCountOne() {
        return chatRoomRepository.findChatRoomsWithUserCountOne();
    }

}
