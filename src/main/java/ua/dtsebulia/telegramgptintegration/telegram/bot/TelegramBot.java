package ua.dtsebulia.telegramgptintegration.telegram.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
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

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            Client client = getClientFromUpdate(update);
            saveClient(client);

            String response;
            if (messageText.equals("/start")) {
                try {
                    response = sendWelcomeMessage(client.getId());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    response = gptController.sendTestMessage(messageText);
                    sendMessage(client.getId(), response);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            logChatMessage(client, messageText, response);
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
        long clientId = update.getMessage().getFrom().getId();
        String userName = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        String imgUrl;

        try {
            imgUrl = getClientProfilePhoto(clientId);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return Client.builder()
                .id(clientId)
                .username(userName)
                .firstName(firstName)
                .lastName(lastName)
                .imgUrl(imgUrl)
                .build();
    }

    private String getClientProfilePhoto(long clientId) throws TelegramApiException {

        GetUserProfilePhotos getUserProfilePhotos = new GetUserProfilePhotos();
        getUserProfilePhotos.setUserId(clientId);
        getUserProfilePhotos.setLimit(1);

        UserProfilePhotos userProfilePhotos = execute(getUserProfilePhotos);

        if (userProfilePhotos.getTotalCount() > 0) {
            PhotoSize photoSize = userProfilePhotos.getPhotos().get(0).stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null);

            if (photoSize != null) {
                GetFile getFile = new GetFile();
                getFile.setFileId(photoSize.getFileId());

                File file = execute(getFile);

                return file.getFileUrl(config.getToken());
            }
        } else {
            return null;
        }
        return null;
    }

    private void saveClient(Client client) {
        if (clientRepository.findById(client.getId()).isEmpty()) {
            clientRepository.save(client);
        }
    }

    private String sendWelcomeMessage(long clientId) throws TelegramApiException {
        String welcomeMessage = "Hi! I'm a test bot created by @d3n41kk that uses OpenAI API. My name is Alzheimer because I can remember only one of your messages. Ask any of your questions, and I'll try to answer them. Please note that the waiting time for a response can last up to a minute due to the time of requests.";
        sendMessage(clientId, welcomeMessage);
        return welcomeMessage;
    }

    private void sendMessage(long clientId, String textToSend) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(clientId));
        message.setText(textToSend);

        execute(message);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    public void sendMessageToClient(long clientId, String message) {
        try {
            sendMessage(clientId, message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

