package ua.dtsebulia.telegramgptintegration.telegram.client.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.ChatLog;
import ua.dtsebulia.telegramgptintegration.telegram.client.repository.ChatLogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatLogService {

    private final ChatLogRepository chatLogRepository;

    public List<ChatLog> getChatLogs() {
        log.info("ChatLogService :: getChatLogs method called");
        return chatLogRepository.findAll();
    }

    public List<ChatLog> getChatLogsByClientId(String id) {
        log.info("ChatLogService :: getChatLogsByClientId method called with id {}", id);
        return chatLogRepository.findAllByClientId(Long.parseLong(id));
    }
}
