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
        } else {
            return MessageType.UNKNOWN;
        }
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
        UNKNOWN
    }


    public static SendVideoNote createSendVideoNoteMessage(VideoNote videoNote, Long receiver) {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(videoNote.getFileId());
        SendVideoNote sendVideoNoteMessage = new SendVideoNote();
        sendVideoNoteMessage.setChatId(String.valueOf(receiver));
        sendVideoNoteMessage.setVideoNote(inputFile);
        return sendVideoNoteMessage;
    }

    public static SendAudio createSendAudioMessage(Audio audio, Message message, Long receiver) {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(audio.getFileId());
        SendAudio sendAudioMessage = new SendAudio();
        sendAudioMessage.setChatId(String.valueOf(receiver));
        sendAudioMessage.setAudio(inputFile);
        if (message.hasText()) {
            sendAudioMessage.setCaption(message.getCaption());
        }
        return sendAudioMessage;
    }

    public static SendVoice createSendVoiceMessage(Message message, Long receiver) {
        Voice voice = message.getVoice();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(voice.getFileId());
        SendVoice sendVoiceMessage = new SendVoice();
        sendVoiceMessage.setChatId(String.valueOf(receiver));
        sendVoiceMessage.setVoice(inputFile);
        return sendVoiceMessage;
    }


    public static SendAnimation createSendAnimationMessage(Message message, Long receiver) {
        Animation animation = message.getAnimation();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(animation.getFileId());
        SendAnimation sendAnimationMessage = new SendAnimation();
        sendAnimationMessage.setChatId(String.valueOf(receiver));
        sendAnimationMessage.setAnimation(inputFile);
        if (message.hasText()) {
            sendAnimationMessage.setCaption(message.getCaption());
        }
        return sendAnimationMessage;
    }


    public static SendVideo createSendVideoMessage(Message message, Long receiver) {
        Video video = message.getVideo();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(video.getFileId());
        SendVideo sendVideoMessage = new SendVideo();
        sendVideoMessage.setChatId(String.valueOf(receiver));
        sendVideoMessage.setVideo(inputFile);
        if (message.hasText()) {
            sendVideoMessage.setCaption(message.getCaption());
        }
        return sendVideoMessage;
    }

    public static SendPhoto createSendPhotoMessage(Message message, Long receiver, List<PhotoSize> photos) {
        PhotoSize largestPhoto = photos.get(photos.size() - 1);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(largestPhoto.getFileId());
        SendPhoto sendPhotoMessage = new SendPhoto();
        sendPhotoMessage.setChatId(String.valueOf(receiver));
        sendPhotoMessage.setPhoto(inputFile);
        if (message.hasText()) {
            sendPhotoMessage.setCaption(message.getCaption());
        }
        return sendPhotoMessage;
    }

    public static SendSticker createSendStickerMessage(Sticker sticker, Long receiver) {
        SendSticker sendStickerMessage = new SendSticker();
        sendStickerMessage.setChatId(String.valueOf(receiver));
        sendStickerMessage.setSticker(new InputFile(sticker.getFileId()));
        return sendStickerMessage;
    }

    public static SendMessage createSendMessage(Message message, Long receiver) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(receiver));
        sendMessage.setText(message.getText());
        return sendMessage;
    }
}
