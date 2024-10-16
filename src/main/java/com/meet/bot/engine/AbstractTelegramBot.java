package com.meet.bot.engine;

import com.meet.bot.domain.User;
import com.meet.bot.exception.TelegramSendException;
import com.meet.bot.utils.MessageUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.meet.bot.constants.Constants.BOHDAN_ID;
import static com.meet.bot.utils.MessageUtils.*;

@CommonsLog
public abstract class AbstractTelegramBot extends TelegramLongPollingBot {


    private Message send(SendMessage message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private Message send(SendPhoto message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private Message send(SendSticker message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private Message send(SendVideo message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private Message send(SendAnimation message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private Message send(SendVoice message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private Message send(SendVideoNote message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private Message send(SendAudio message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private Message send(SendDocument message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
        return null;
    }

    private void handleError(TelegramApiException e) {
        throw new TelegramSendException(e);
    }

    public Message send(Object message) {
        if (message instanceof SendMessage) {
            return send((SendMessage) message);
        } else if (message instanceof SendPhoto) {
            return send((SendPhoto) message);
        } else if (message instanceof SendSticker) {
            return send((SendSticker) message);
        } else if (message instanceof SendVideo) {
            return send((SendVideo) message);
        } else if (message instanceof SendAnimation) {
            return send((SendAnimation) message);
        } else if (message instanceof SendVoice) {
            return send((SendVoice) message);
        } else if (message instanceof SendVideoNote) {
            return send((SendVideoNote) message);
        } else if (message instanceof SendAudio) {
            return send((SendAudio) message);
        } else if (message instanceof SendDocument) {
            return send((SendDocument) message);
        }
        throw new RuntimeException(message.getClass() + "is not implemented");

    }

    public Message sendToAdmin(Object message) {
        if (message instanceof SendMessage) {

            return send((SendMessage) message);
        } else if (message instanceof SendPhoto) {
            return send((SendPhoto) message);
        } else if (message instanceof SendSticker) {
            return send((SendSticker) message);
        } else if (message instanceof SendVideo) {
            return send((SendVideo) message);
        } else if (message instanceof SendAnimation) {
            return send((SendAnimation) message);
        } else if (message instanceof SendVoice) {
            return send((SendVoice) message);
        } else if (message instanceof SendVideoNote) {
            return send((SendVideoNote) message);
        } else if (message instanceof SendAudio) {
            return send((SendAudio) message);
        }
        throw new RuntimeException(message.getClass() + "is not implemented");

    }

    public Message sendTextMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return send(message);

    }

    public void notifyAdmin(String senderTelegramId, String receiverTelegramId) {
        sendTextMessage(BOHDAN_ID, senderTelegramId + " to " + receiverTelegramId + " :");
    }

    public void notifyAdmin(String message) {
        sendTextMessage(BOHDAN_ID, message);
    }

    public void notifyAll(String text, List<String> receivers) {
        for (String receiver : receivers) {
            try {
                sendTextMessage(receiver, text);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public void forwardTextMessage(Message message, String senderTelegramId, String receiverTelegramId) {
        SendMessage sendMessage = createSendMessage(message, receiverTelegramId);
        send(sendMessage);
        sendMessage.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendMessage);
    }

    public void forwardPhotoMessage(Message message, String senderTelegramId, String receiverTelegramId) {
        SendPhoto sendPhotoMessage = createSendPhotoMessage(message, senderTelegramId, message.getPhoto());
        send(sendPhotoMessage);
        sendPhotoMessage.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendPhotoMessage);
    }


    protected void forwardStickerMessage(Message message, String senderTelegramId, String receiverTelegramId) {
        SendSticker sendStickerMessage = createSendStickerMessage(message.getSticker(), receiverTelegramId);
        send(sendStickerMessage);
        sendStickerMessage.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendStickerMessage);
    }

    protected void forwardAnimationMessage(Message message, String senderTelegramId, String receiverTelegramId) {
        SendAnimation sendAnimationMessage = createSendAnimationMessage(message, receiverTelegramId);
        send(sendAnimationMessage);
        sendAnimationMessage.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendAnimationMessage);
    }

    protected void forwardVideoMessage(Message message, String senderTelegramId, String receiverTelegramId) {
        SendVideo sendVideoMessage = createSendVideoMessage(message, receiverTelegramId);
        send(sendVideoMessage);
        sendVideoMessage.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendVideoMessage);
    }

    protected void forwardAudioMessage(Message message, String senderTelegramId, String receiverTelegramId) {
        SendAudio sendAudioMessage = createSendAudioMessage(message.getAudio(), message, receiverTelegramId);
        send(sendAudioMessage);
        sendAudioMessage.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendAudioMessage);
    }

    protected void forwardVoiceMessage(Message message, String senderTelegramId, String receiverTelegramId) {
        SendVoice sendVoiceMessage = createSendVoiceMessage(message, receiverTelegramId);

        send(sendVoiceMessage);
        sendVoiceMessage.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendVoiceMessage);

    }


    protected void forwardVideoNoteMessage(Message message, String senderTelegramId, String receiverTelegramId) {
        SendVideoNote sendVideoNoteMessage = createSendVideoNoteMessage(message.getVideoNote(), receiverTelegramId);
        send(sendVideoNoteMessage);
        sendVideoNoteMessage.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendVideoNoteMessage);
    }

    public void forwardDocument(Message message, String senderTelegramId, String receiverTelegramId) {
        SendDocument sendDocument = createSendDocumentMessage(message.getDocument(), receiverTelegramId);
        send(sendDocument);
        sendDocument.setChatId(BOHDAN_ID);
        notifyAdmin(senderTelegramId, receiverTelegramId);
        send(sendDocument);
    }

    protected void forward(Message message, User user, User receiver) {
        MessageUtils.MessageType messageType = MessageUtils.determineMessageType(message);
        switch (messageType) {
            case TEXT -> forwardTextMessage(message, user.getTelegramId(), receiver.getTelegramId());
            case PHOTO -> forwardPhotoMessage(message, user.getTelegramId(), receiver.getTelegramId());
            case VIDEO -> forwardVideoMessage(message, user.getTelegramId(), receiver.getTelegramId());
            case ANIMATION -> forwardAnimationMessage(message, user.getTelegramId(), receiver.getTelegramId());
            case VOICE -> forwardVoiceMessage(message, user.getTelegramId(), receiver.getTelegramId());
            case AUDIO -> forwardAudioMessage(message, user.getTelegramId(), receiver.getTelegramId());
            case VIDEO_NOTE -> forwardVideoNoteMessage(message, user.getTelegramId(), receiver.getTelegramId());
            case STICKER -> forwardStickerMessage(message, user.getTelegramId(), receiver.getTelegramId());
            case DOCUMENT -> forwardDocument(message, user.getTelegramId(), receiver.getTelegramId());
            case UNKNOWN -> {
                throw new RuntimeException("not implemented");
            }
        }
    }


}
