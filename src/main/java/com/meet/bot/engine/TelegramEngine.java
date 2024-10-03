package com.meet.bot.engine;


import com.meet.bot.constants.Constants;
import com.meet.bot.domain.ChatRoom;
import com.meet.bot.domain.User;
import com.meet.bot.service.ChatRoomService;
import com.meet.bot.service.UserService;
import com.meet.bot.utils.CommandUtils;
import com.meet.bot.utils.MessageUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.meet.bot.constants.Constants.*;
import static com.meet.bot.utils.MessageUtils.*;


@Component
public class TelegramEngine extends TelegramLongPollingBot {

    private final UserService userService;

    private final ChatRoomService chatRoomService;

    public TelegramEngine(UserService userService, ChatRoomService chatRoomService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
    }

    @Override
    public String getBotUsername() {
        return "YourBotUsername"; // Replace with your bot username
    }

    @Override
    public String getBotToken() {
        return "7592966663:AAGDVVh8WZwfnt7VksHlZ6gGSAhy_qUqmoo"; // Replace with your bot token
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (CommandUtils.isCommand(update, Constants.START)) {
            processStart(update);
            return;
        }
        processMessage(update);
    }

    private void processMessage(Update update) {
        User user = userService.getOrCreate(update);
        ChatRoom chatRoom = chatRoomService.createOrGetChatRoom(user);
        List<Long> list = chatRoom.getUsers().stream()
                .map(User::getTelegramId)
                .filter(e -> !Objects.equals(e, user.getTelegramId()))
                .toList();

        if (CollectionUtils.isEmpty(chatRoom.getUsers())) {
            return;
        }

        for (Long receiver : list) {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                MessageUtils.MessageType messageType = MessageUtils.determineMessageType(message);
                switch (messageType) {
                    case TEXT -> forwardTextMessage(message, receiver);
                    case PHOTO -> forwardPhotoMessage(message, receiver);
                    case VIDEO -> forwardVideoMessage(message, receiver);
                    case ANIMATION -> forwardAnimationMessage(message, receiver);
                    case VOICE -> forwardVoiceMessage(message, receiver);
                    case AUDIO -> forwardAudioMessage(message, receiver);
                    case VIDEO_NOTE -> forwardVideoNoteMessage(message, receiver);
                    case STICKER -> forwardStickerMessage(message, receiver);
                    case UNKNOWN -> {
                        // Handle unknown type or ignore
                    }
                }
            }
        }
    }

    private void forwardVideoNoteMessage(Message message, Long receiver) {
        SendVideoNote sendVideoNoteMessage = createSendVideoNoteMessage(message.getVideoNote(), receiver);
        try {
            execute(sendVideoNoteMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    private void forwardAudioMessage(Message message, Long receiver) {
        SendAudio sendAudioMessage = createSendAudioMessage(message.getAudio(), message, receiver);
        try {
            execute(sendAudioMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    private void forwardVoiceMessage(Message message, Long receiver) {
        SendVoice sendVoiceMessage = createSendVoiceMessage(message, receiver);
        try {
            execute(sendVoiceMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    private void forwardAnimationMessage(Message message, Long receiver) {
        SendAnimation sendAnimationMessage = createSendAnimationMessage(message, receiver);
        try {
            execute(sendAnimationMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    private void forwardVideoMessage(Message message, Long receiver) {
        SendVideo sendVideoMessage = createSendVideoMessage(message, receiver);

        try {
            execute(sendVideoMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    private void processStart(Update update) {
        User user = userService.getOrCreate(update);
        ChatRoom chatRoom = chatRoomService.createOrGetChatRoom(user);
        if (chatRoom.getUsers().size() == 1) {
            sendTextMessage(user.getTelegramId(), String.format(YOUR_NAME_IS, user.getSystemName()));
            sendTextMessage(user.getTelegramId(), SEARCHING_FOR_ROOMMATE);
        } else {
            String room = chatRoom.getUsers().stream().map(User::getSystemName).collect(Collectors.joining(AND_SEPARATOR));
            sendTextMessage(user.getTelegramId(), String.format(PEOPLE_IN_CHAT, room, user.getSystemName()));
        }
    }

    private void sendTextMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void forwardTextMessage(Message message, Long receiver) {
        SendMessage sendMessage = createSendMessage(message, receiver);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    private void forwardPhotoMessage(Message message, Long receiver) {
        SendPhoto sendPhotoMessage = createSendPhotoMessage(message, receiver, message.getPhoto());
        try {
            execute(sendPhotoMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    private void forwardStickerMessage(Message message, Long receiver) {
        SendSticker sendStickerMessage = createSendStickerMessage(message.getSticker(), receiver);
        try {
            execute(sendStickerMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    private void handleError(TelegramApiException e) {
        // Handle error (e.g., logging or displaying an error message)
        e.printStackTrace();
    }

}