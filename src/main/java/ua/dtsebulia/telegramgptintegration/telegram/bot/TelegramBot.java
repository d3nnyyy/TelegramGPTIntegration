package ua.dtsebulia.telegramgptintegration.telegram.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.dtsebulia.telegramgptintegration.gpt.controller.GPTController;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramConfig config;
    private final GPTController gptController;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long clientId = update.getMessage().getFrom().getId();

            if (messageText.equals("/start")) {
                try {
                    sendWelcomeMessage(clientId);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    String response = gptController.sendTestMessage(messageText);
                    sendMessage(clientId, response);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendWelcomeMessage(long clientId) throws TelegramApiException {
        String welcomeMessage = "Hi! I'm a test bot created by @d3n41kk that uses OpenAI API. My name is Alzheimer because I can remember only one of your messages. Ask any of your questions, and I'll try to answer them. Please note that the waiting time for a response can last up to a minute due to the time of requests.";
        sendMessage(clientId, welcomeMessage);
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

}

