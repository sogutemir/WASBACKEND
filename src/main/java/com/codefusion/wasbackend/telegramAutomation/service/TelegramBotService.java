package com.codefusion.wasbackend.telegramAutomation.service;

import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.util.Date;

@Component
public class TelegramBotService extends TelegramLongPollingBot {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
    private static final String BOT_TOKEN = "6927765363:AAHiqT2la5tYQ_lWRBlkMpjAhv03sGybCj0";
    private static final String BOT_NAME = "WASTelegram_BOT";

    public TelegramBotService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            processMessage(update);
        }
    }

    private void processMessage(Update update) {
        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        String debugMessage = messageText.length() > 16 ? messageText.substring(0, 16) : messageText;
        logger.debug("Received message '{}' in chat {}.", debugMessage, chatId);

        if (messageText.startsWith("/start")) {
            processStartCommand(messageText, chatId);
        } else if (messageText.startsWith("/id")) {
            sendMessage(chatId, "Telegram ID'niz : '" + chatId + "'.");
        } else {
            sendMessage(chatId, "Not implemented yet.");
        }
    }

    private void processStartCommand(String messageText, Long chatId) {
        String[] commandParts = messageText.split(" ");

        if (commandParts.length < 2) {
            sendInvalidFormatMessage(chatId);
            return;
        }

        String[] paramParts = commandParts[1].split("_");

        if (paramParts.length < 2) {
            sendInvalidFormatMessage(chatId);
            return;
        }

        Long userId = Long.parseLong(paramParts[0]);
        String verificationCode = paramParts[1];

        logger.debug("/start\n\tuserId: {}\n\tcode: {}", userId, verificationCode);

        boolean isVerified = verifyUser(paramParts[0], paramParts[1], userId, chatId);

        sendMessage(chatId, isVerified ? "Doğrulama başarılı.": "Doğrulama başarısız.");
    }

    private void sendInvalidFormatMessage(Long chatId) {
        sendMessage(chatId, "Geçersiz format.");
    }


    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean verifyUser(String userGuid, String verificationCode, Long userId, Long chatId) {
        long startTime = System.currentTimeMillis();

        UserEntity userEntity = userRepository.findById(Long.parseLong(userGuid)).orElse(null);

        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.debug("[GetUser] Elapsed time: {} ms", elapsedTime);

        if (userEntity == null) {
            logger.debug("User not found: {}", userGuid);
            return false;
        }

        logger.debug("User found: {}", userEntity.getEmail());

        if (!verificationCode.equals(userEntity.getActivationRequestCode())) {
            logger.debug("Verification code mismatched: {} != {}", verificationCode, userEntity.getActivationRequestCode());
            return false;
        }

        logger.debug("Verification code matched: {}", verificationCode);

        userEntity.setActivationRequestCode("");

        if (userEntity.getTelegramId() != null && userEntity.getTelegramId() != 0) {
            logger.debug("User already has a telegram id: {}", userEntity.getTelegramId());
        }
        userEntity.setTelegramId(chatId);
        userEntity.setTelegramLinkTime(Date.from(Instant.now()));


        userRepository.save(userEntity);

        logger.debug("User telegram id inserted/updated successfully.");
        return true;
    }


    public static String GenerateTelegramDeeplink(String command, String botName, String... parameters)
    {
        if (botName == null || botName.isEmpty())
        {
            botName = BOT_NAME;
        }
        if (command == null || command.isEmpty())
        {
            command = "start";
        }

        String link = "https://t.me/" + botName + "?" + command + "=";

        if (parameters != null && parameters.length > 0)
        {
            String joinedParams = String.join("_", parameters);

            if (joinedParams.length() > 64)
            {
                throw new IllegalArgumentException("Parameter length exceeds 64 characters.");
            }

            link += joinedParams;
        }

        return link;
    }


    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}