package com.meet.bot.engine;


import com.meet.bot.constants.Constants;
import com.meet.bot.domain.ChatRoom;
import com.meet.bot.domain.User;
import com.meet.bot.exception.TelegramSendException;
import com.meet.bot.service.ChatRoomService;
import com.meet.bot.service.UserService;
import com.meet.bot.utils.CommandUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.meet.bot.constants.Constants.*;


@Component
public class TelegramEngine extends AbstractTelegramBot {

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
        if (CommandUtils.isCommand(update, Constants.NEW_CHAT)) {
            refreshChat(update);
            return;
        }
        if (CommandUtils.isCommand(update, Constants.CALL_ADMIN)) {
            callAdmin(update);
            return;
        }
        processMessage(update);
    }

    private void callAdmin(Update update) {
        Optional<User> user = userService.getUser(update);
        user.ifPresent(value -> sendTextMessage(value.getTelegramId(), "@kartoplagangster"));
    }

    private void refreshChat(Update update) {
        User user = userService.getOrCreate(update);
        Optional<ChatRoom> chatRoom = chatRoomService.findChatRoomByUser(user);
        if (chatRoom.isEmpty() || chatRoom.get().getUsers().size() == 1) {
            return;
        }

        Set<User> users = chatRoom.get().getUsers();
        notifyAll(PEOPLE_LEFT_CHAT, users.stream().map(User::getTelegramId).collect(Collectors.toList()));

        Set<ChatRoom> collect = users.stream().flatMap(e -> e.getChatRooms().stream()).collect(Collectors.toSet());
        chatRoomService.deleteChatRooms(collect);
    }

    private void processMessage(Update update) {
        User user = userService.getOrCreate(update);

        ChatRoom userChatRoom;
        Optional<ChatRoom> chatRoom = chatRoomService.findChatRoomByUser(user);
        if (chatRoom.isEmpty()) {
            userChatRoom = chatRoomService.createOrFindChatRoomForUser(user);
            if (userChatRoom.getUsers().size() > 1) {
                String roomMembers = userChatRoom.getUsers().stream().map(User::getSystemName).collect(Collectors.joining(AND_SEPARATOR));
                userChatRoom.getUsers().forEach(u -> sendTextMessage(u.getTelegramId(), String.format(PEOPLE_IN_CHAT, roomMembers, u.getSystemName())));
            }
        } else {
            userChatRoom = chatRoom.get();
        }

        Set<User> users = userChatRoom.getUsers().stream().filter(
                e -> !Objects.equals(e.getTelegramId(), user.getTelegramId())).collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(userChatRoom.getUsers()) ||
                userChatRoom.getUsers().size() == 1) {
            sendTextMessage(user.getTelegramId(), String.format(YOUR_NAME_IS, user.getSystemName()));
            sendTextMessage(user.getTelegramId(), SEARCHING_FOR_ROOMMATE);
            return;
        }

        for (User receiver : users) {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                try {
                    forward(message, user, receiver);
                } catch (TelegramSendException telegramSendException) {
                    if (!StringUtils.isBlank(telegramSendException.getMessage())) {
                        if (telegramSendException.getMessage().contains(Constants.BOT_BLOCKED)) {
                            refreshChatAndDeleteUser(update, receiver);
                        }
                    }
                }
            }
        }
    }

    private void refreshChatAndDeleteUser(Update update, User receiver) {
        refreshChat(update);
        userService.delete(receiver);
        notifyAdmin("DELETED : " + receiver.toString());
    }

    private void processStart(Update update) {
        User user = userService.getOrCreate(update);
        ChatRoom chatRoom = chatRoomService.createOrGetChatRoom(user);
        sendTextMessage(user.getTelegramId(), String.format(YOUR_NAME_IS, user.getSystemName()));
        if (chatRoom.getUsers().size() == 1) {
            sendTextMessage(user.getTelegramId(), SEARCHING_FOR_ROOMMATE);
        } else {
            String roomMembers = chatRoom.getUsers().stream().map(User::getSystemName).collect(Collectors.joining(AND_SEPARATOR));
            chatRoom.getUsers().forEach(u -> sendTextMessage(u.getTelegramId(), String.format(PEOPLE_IN_CHAT, roomMembers, u.getSystemName())));
        }
    }

    private void start(User user) {
        ChatRoom chatRoom = chatRoomService.createOrGetChatRoom(user);
        sendTextMessage(user.getTelegramId(), String.format(YOUR_NAME_IS, user.getSystemName()));
        if (chatRoom.getUsers().size() == 1) {
            sendTextMessage(user.getTelegramId(), SEARCHING_FOR_ROOMMATE);
        } else {
            String roomMembers = chatRoom.getUsers().stream().map(User::getSystemName).collect(Collectors.joining(AND_SEPARATOR));
            chatRoom.getUsers().forEach(u -> sendTextMessage(u.getTelegramId(), String.format(PEOPLE_IN_CHAT, roomMembers, u.getSystemName())));
        }
    }

    @Scheduled(cron = "0 0/30 * * * *") // This cron expression triggers every minute
    public void performUserShuffle() {
        List<User> users = userService.findUsersWithNoChatRoomsOrSingleUserChatRooms();
        for (User user : users) {
            start(user);
        }
    }
}