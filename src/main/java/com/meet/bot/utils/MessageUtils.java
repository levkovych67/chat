package com.meet.bot.utils;

import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.List;

public class MessageUtils {

    public static MessageType determineMessageType(Message message) {
        if (message.hasText()) {
            return MessageType.TEXT;
        } else if (message.hasPhoto()) {
            return MessageType.PHOTO;
        } else if (message.hasVideo()) {
            return MessageType.VIDEO;
        } else if (message.hasAnimation()) {
            return MessageType.ANIMATION;
        } else if (message.hasVoice()) {
            return MessageType.VOICE;
        } else if (message.hasAudio()) {
            return MessageType.AUDIO;
        } else if (message.hasVideoNote()) {
            return MessageType.VIDEO_NOTE;
        } else if (message.hasSticker()) {
            return MessageType.STICKER;
        } else if (message.hasDocument()) {
            return MessageType.DOCUMENT;
        } else {
            return MessageType.UNKNOWN;
        }
    }

    public static SendVideoNote createSendVideoNoteMessage(VideoNote videoNote, String chatId) {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(videoNote.getFileId());
        SendVideoNote sendVideoNoteMessage = new SendVideoNote();
        sendVideoNoteMessage.setChatId(chatId);
        sendVideoNoteMessage.setVideoNote(inputFile);
        return sendVideoNoteMessage;
    }

    public static SendDocument createSendDocumentMessage(Document document, String chatId) {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(document.getFileId());
        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(inputFile);
        sendDocument.setChatId(chatId);
        return sendDocument;
    }

    public static SendAudio createSendAudioMessage(Audio audio, Message message, String chatId) {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(audio.getFileId());
        SendAudio sendAudioMessage = new SendAudio();
        sendAudioMessage.setChatId(chatId);
        sendAudioMessage.setAudio(inputFile);
        if (message.hasText()) {
            sendAudioMessage.setCaption(message.getCaption());
        }
        return sendAudioMessage;
    }

    public static SendVoice createSendVoiceMessage(Message message, String chatId) {
        Voice voice = message.getVoice();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(voice.getFileId());
        SendVoice sendVoiceMessage = new SendVoice();
        sendVoiceMessage.setChatId(chatId);
        sendVoiceMessage.setVoice(inputFile);
        return sendVoiceMessage;
    }

    public static SendAnimation createSendAnimationMessage(Message message, String chatId) {
        Animation animation = message.getAnimation();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(animation.getFileId());
        SendAnimation sendAnimationMessage = new SendAnimation();
        sendAnimationMessage.setChatId(chatId);
        sendAnimationMessage.setAnimation(inputFile);
        if (message.hasText()) {
            sendAnimationMessage.setCaption(message.getCaption());
        }
        return sendAnimationMessage;
    }

    public static SendVideo createSendVideoMessage(Message message, String chatId) {
        Video video = message.getVideo();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(video.getFileId());
        SendVideo sendVideoMessage = new SendVideo();
        sendVideoMessage.setChatId(chatId);
        sendVideoMessage.setVideo(inputFile);
        if (message.hasText()) {
            sendVideoMessage.setCaption(message.getCaption());
        }
        return sendVideoMessage;
    }

    public static SendPhoto createSendPhotoMessage(Message message, String chatId, List<PhotoSize> photos) {
        PhotoSize largestPhoto = photos.get(photos.size() - 1);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(largestPhoto.getFileId());
        SendPhoto sendPhotoMessage = new SendPhoto();
        sendPhotoMessage.setChatId(chatId);
        sendPhotoMessage.setPhoto(inputFile);
        if (message.hasText()) {
            sendPhotoMessage.setCaption(message.getCaption());
        }
        return sendPhotoMessage;
    }

    public static SendSticker createSendStickerMessage(Sticker sticker, String chatId) {
        SendSticker sendStickerMessage = new SendSticker();
        sendStickerMessage.setChatId(chatId);
        sendStickerMessage.setSticker(new InputFile(sticker.getFileId()));
        return sendStickerMessage;
    }

    public static SendMessage createSendMessage(Message message, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message.getText());
        return sendMessage;
    }

    public enum MessageType {
        TEXT,
        PHOTO,
        VIDEO,
        ANIMATION,
        VOICE,
        AUDIO,
        VIDEO_NOTE,
        STICKER,
        DOCUMENT, UNKNOWN
    }
}
