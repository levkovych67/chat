package com.meet.bot.exception;

public class TelegramSendException extends RuntimeException {

    public TelegramSendException(Exception e) {
        super(e);
    }
}
