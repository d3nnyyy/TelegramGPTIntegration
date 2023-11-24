package ua.dtsebulia.telegramgptintegration.gpt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ua.dtsebulia.telegramgptintegration.gpt.dto.Request;
import ua.dtsebulia.telegramgptintegration.gpt.dto.Response;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class GPTController {

    private final RestTemplate restTemplate;

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.url}")
    private String url;

    private static final String NO_MEANINGFUL_RESPONSE_MESSAGE = "Sorry, I couldn't generate a meaningful response at the moment. Please try again later.";
    private static final String SOMETHING_WENT_WRONG_MESSAGE = "Oops! Something went wrong while processing your request. Please try again later.";

    @PostMapping
    public String sendTestMessage(@RequestBody String message) {
        // Log the received message
        log.info("Received message: {}", message);

        return executeGptMessage(message);
    }

    private String executeGptMessage(String message) {
        Request request = new Request(model, message);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Request> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Response.class
            );

            Response response = responseEntity.getBody();

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String gptResponse = response.getChoices().get(0).getMessage().getContent();

                // Log the GPT response
                log.info("GPT response: {}", gptResponse);

                return gptResponse;
            } else {
                // Log the no meaningful response message
                log.error("No meaningful response message: {}", NO_MEANINGFUL_RESPONSE_MESSAGE);

                return NO_MEANINGFUL_RESPONSE_MESSAGE;
            }
        } catch (Exception e) {
            // Log the exception
            log.error("Exception: {}", e.getMessage());

            return SOMETHING_WENT_WRONG_MESSAGE;
        }
    }
}
