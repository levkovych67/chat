package com.meet.bot.service;

import com.meet.bot.domain.User;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface UserService {

    public User getOrCreate(Update update);
}

