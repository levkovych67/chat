package com.meet.bot.utils;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public class CommandUtils {

    public static boolean isCommand(Update update, String command) {
        try {
            if (Objects.equals(update.getMessage().getText(), command)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
