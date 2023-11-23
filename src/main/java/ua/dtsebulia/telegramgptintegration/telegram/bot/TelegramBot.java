package ua.dtsebulia.telegramgptintegration.telegram.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.dtsebulia.telegramgptintegration.gpt.controller.GPTController;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.ChatLog;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.Client;
import ua.dtsebulia.telegramgptintegration.telegram.client.repository.ChatLogRepository;
import ua.dtsebulia.telegramgptintegration.telegram.client.repository.ClientRepository;

import java.time.LocalDateTime;
import java.util.Comparator;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramConfig config;
    private final GPTController gptController;
    private final ClientRepository clientRepository;
    private final ChatLogRepository chatLogRepository;

    private static final String START_COMMAND = "/start";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            Client client = getClientFromUpdate(update);
            saveClientIfNotExists(client);

            String response;
            if (messageText.equals(START_COMMAND)) {
                response = sendWelcomeMessage(client.getId());
            } else {
                response = processNonStartCommand(messageText, client);
            }

            logChatMessage(client, messageText, response);
        }
    }

    private String processNonStartCommand(String messageText, Client client) {
        try {
            String response = gptController.sendTestMessage(messageText);
            sendMessage(client.getId(), response);
            return response;
        } catch (TelegramApiException e) {
            log.error("Error sending message to client {}: {}", client.getId(), e.getMessage(), e);
            return "An error occurred while processing your request.";
        }
    }

    private void logChatMessage(Client client, String message, String response) {

        ChatLog chatLog = ChatLog
                .builder()
                .timestamp(LocalDateTime.now())
                .clientId(client.getId())
                .message(message)
                .response(response)
                .build();

        chatLogRepository.save(chatLog);
    }

    private Client getClientFromUpdate(Update update) {

        User user = update.getMessage().getFrom();

        long clientId = user.getId();
        String userName = user.getUserName();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String imgUrl = getClientProfilePhoto(clientId);

        return Client.builder()
                .id(clientId)
                .username(userName)
                .firstName(firstName)
                .lastName(lastName)
                .imgUrl(imgUrl)
                .build();
    }

    private String getClientProfilePhoto(long clientId) {
        try {
            GetUserProfilePhotos getUserProfilePhotos = new GetUserProfilePhotos();
            getUserProfilePhotos.setUserId(clientId);
            getUserProfilePhotos.setLimit(1);

            UserProfilePhotos userProfilePhotos = execute(getUserProfilePhotos);

            if (userProfilePhotos != null && userProfilePhotos.getTotalCount() > 0) {
                PhotoSize photoSize = userProfilePhotos.getPhotos().get(0).stream()
                        .max(Comparator.comparing(PhotoSize::getFileSize))
                        .orElse(null);

                if (photoSize != null) {
                    GetFile getFile = new GetFile();
                    getFile.setFileId(photoSize.getFileId());

                    File file = execute(getFile);

                    return file.getFileUrl(config.getToken());
                }
            }
        } catch (TelegramApiException e) {
            log.error("Error getting profile photo for client {}: {}", clientId, e.getMessage(), e);
        }
        return null;
    }

    private void saveClientIfNotExists(Client client) {
        clientRepository.findById(client.getId()).ifPresentOrElse(
                existingClient -> log.debug("Client {} already exists", existingClient.getId()),
                () -> {
                    clientRepository.save(client);
                    log.debug("Client {} saved", client.getId());
                }
        );
    }

    private String sendWelcomeMessage(long clientId) {
        String welcomeMessage = "Hi! I'm a test bot created by @d3n41kk that uses OpenAI API. " +
                "My name is Alzheimer because I can remember only one of your messages. " +
                "Ask any of your questions, and I'll try to answer them. " +
                "Please note that the waiting time for a response can last up to a minute due to the time of requests.";

        try {
            sendMessage(clientId, welcomeMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending welcome message to client {}: {}", clientId, e.getMessage(), e);
        }
        return welcomeMessage;
    }

    private void sendMessage(long clientId, String textToSend) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(clientId));
        message.setText(textToSend);
        execute(message);
    }

    public void sendMessageToClient(long clientId, String message) {
        try {
            sendMessage(clientId, message);
        } catch (TelegramApiException e) {
            log.error("Error sending message to client {}: {}", clientId, e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}

