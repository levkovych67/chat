package com.meet.bot.service.impl;

import com.meet.bot.domain.User;
import com.meet.bot.exception.UserBlockedException;
import com.meet.bot.mapper.TelegramToDomainMapper;
import com.meet.bot.repo.UserRepository;
import com.meet.bot.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.meet.bot.constants.Constants.BLOCKED;

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


}
