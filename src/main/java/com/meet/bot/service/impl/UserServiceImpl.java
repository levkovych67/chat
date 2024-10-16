package com.meet.bot.service.impl;

import com.meet.bot.domain.User;
import com.meet.bot.mapper.TelegramToDomainMapper;
import com.meet.bot.repo.UserRepository;
import com.meet.bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User getOrCreate(Update update) {
        User user = TelegramToDomainMapper.getUserFromUpdate(update).orElseThrow(() -> new IllegalArgumentException("User not found in update"));
        return userRepository.findByTelegramId(user.getTelegramId())
                .filter(existingUser -> !existingUser.getBlocked())
                .orElseGet(() -> userRepository.save(user));
    }

    @Override
    public Optional<User> getUser(Update update) {
        User user = TelegramToDomainMapper.getUserFromUpdate(update).orElseThrow(() -> new IllegalArgumentException("User not found in update"));
        Optional<User> byTelegramId = userRepository.findByTelegramId(user.getTelegramId());
        return byTelegramId;
    }

    @Override
    public List<User> findUsersWithNoChatRoomsOrSingleUserChatRooms() {
        return userRepository.findUsersWithoutChatRoomsOrInSingleUserChatRooms();
    }

    @Override
    public void delete(User receiver) {
        userRepository.deleteById(receiver.getId());
    }


}
