package ua.dtsebulia.telegramgptintegration.telegram.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.ChatLog;
import ua.dtsebulia.telegramgptintegration.telegram.client.repository.ChatLogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatLogService {

    private final ChatLogRepository chatLogRepository;

    public List<ChatLog> getChatLogs() {
        return chatLogRepository.findAll();
    }

    public List<ChatLog> getChatLogsByClientId(String id) {
        return chatLogRepository.findAllByClientId(Long.parseLong(id));
    }
}
