package ua.dtsebulia.telegramgptintegration.gpt.controller;

import lombok.RequiredArgsConstructor;
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
                return response.getChoices().get(0).getMessage().getContent();
            } else {
                return NO_MEANINGFUL_RESPONSE_MESSAGE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SOMETHING_WENT_WRONG_MESSAGE;
        }
    }

}
