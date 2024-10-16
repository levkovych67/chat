package com.meet.bot.service;

import com.meet.bot.domain.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;


public interface UserService {

    User getOrCreate(Update update);

    Optional<User> getUser(Update update);

    List<User> findUsersWithNoChatRoomsOrSingleUserChatRooms();

    void delete(User receiver);

}

