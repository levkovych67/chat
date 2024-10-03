package com.meet.bot.mapper;

import com.meet.bot.domain.User;
import com.meet.bot.utils.UserNameGenerator;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;


public class TelegramToDomainMapper {

    public static Optional<User> getUserFromUpdate(Update update){
        try {
            /* Handle different update types */
            if (update.hasMessage()) {
                org.telegram.telegrambots.meta.api.objects.User  user = handleUserFromMessage(update);
                return Optional.ofNullable(telegramUserToDomain(user));
            } else if (update.hasInlineQuery()) {
                org.telegram.telegrambots.meta.api.objects.User  user = handleUserFromInlineQuery(update);
                return Optional.ofNullable(telegramUserToDomain(user));
            } else if (update.hasCallbackQuery()) {
                org.telegram.telegrambots.meta.api.objects.User  user = handleUserFromCallbackQuery(update);
                return Optional.ofNullable(telegramUserToDomain(user));
            } else {
                System.err.println("Unsupported update type. Update: " + update);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    // Method to extract user from a message update
    private static org.telegram.telegrambots.meta.api.objects.User handleUserFromMessage(Update update) {
        try {
            return update.getMessage().getFrom();
        } catch (NullPointerException e) {
            System.err.println("Error: Update is missing message or 'from' field.");
            // Handle the error appropriately
            return null; // Or handle the error differently
        }
    }

    // Method to extract user from an inline query update
    private static org.telegram.telegrambots.meta.api.objects.User  handleUserFromInlineQuery(Update update) {
        try {
            return update.getInlineQuery().getFrom();
        } catch (NullPointerException e) {
            System.err.println("Error: Update is missing inline query or 'from' field.");
            // Handle the error appropriately
            return null; // Or handle the error differently
        }
    }

    // Method to extract user from a callback query update
    private static org.telegram.telegrambots.meta.api.objects.User  handleUserFromCallbackQuery(Update update) {
        try {
            return update.getCallbackQuery().getFrom();
        } catch (NullPointerException e) {
            System.err.println("Error: Update is missing callback query or 'from' field.");
            // Handle the error appropriately
            return null; // Or handle the error differently
        }
    }

    // Helper method to print user details
    private static User telegramUserToDomain(org.telegram.telegrambots.meta.api.objects.User  user) {
        if (user != null) {
            User systemUser = new User();
            systemUser.setTelegramId(user.getId());
            systemUser.setUsername("@" + user.getUserName());
            systemUser.setFirstName(user.getFirstName());
            systemUser.setFirstName(user.getFirstName());
            systemUser.setSystemName(UserNameGenerator.generateName());
            return systemUser;
        } else {
            System.err.println("User not found.");
        }
        return null;
    }

}
