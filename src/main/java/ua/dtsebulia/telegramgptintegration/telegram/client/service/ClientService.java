package ua.dtsebulia.telegramgptintegration.telegram.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dtsebulia.telegramgptintegration.telegram.bot.TelegramBot;
import ua.dtsebulia.telegramgptintegration.telegram.client.exception.ClientNotFoundException;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.ChatLog;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.Client;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.MessageToClient;
import ua.dtsebulia.telegramgptintegration.telegram.client.repository.ChatLogRepository;
import ua.dtsebulia.telegramgptintegration.telegram.client.repository.ClientRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ChatLogRepository chatLogRepository;
    private final TelegramBot telegramBot;

    public Client getClientById(String id) {
        return clientRepository.findById(Long.parseLong(id)).orElseThrow(
                () -> new ClientNotFoundException("Client with id " + id + " not found")
        );
    }

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public void sendMessageToClient(MessageToClient message) {

        if (!clientRepository.existsById(message.getClientId())) {
            throw new ClientNotFoundException("Client with id " + message.getClientId() + " not found");
        }

        ChatLog chatLog = ChatLog
                .builder()
                .timestamp(LocalDateTime.now())
                .clientId(message.getClientId())
                .response(message.getMessage())
                .build();

        telegramBot.sendMessageToClient(message.getClientId(), message.getMessage());
        chatLogRepository.save(chatLog);
    }
}
