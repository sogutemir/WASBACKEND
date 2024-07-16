package com.codefusion.wasbackend.telegramAutomation.service;

import com.codefusion.wasbackend.config.utils.CryptoTool;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TelegramService {

    private final UserRepository userRepository;

    public TelegramService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional()
    public String linkTelegramUser(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }

        String userData = user.getId().toString() + user.getEmail() + CryptoTool.randomKey(16) + user.getName() + user.getAccount().getPassword();
        userData = CryptoTool.toMD5(userData);

        String hash = userData.replace("-", "").substring(0, 8);

        user.setActivationRequestCode(hash);
        user.setIsTelegram(true);
        userRepository.save(user);

        return TelegramBotService.GenerateTelegramDeeplink("start", "", user.getId().toString(), user.getActivationRequestCode());
    }


}
