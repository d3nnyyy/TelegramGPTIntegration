package ua.dtsebulia.telegramgptintegration.telegram.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.ChatLog;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.Client;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.MessageToClient;
import ua.dtsebulia.telegramgptintegration.telegram.client.service.ClientService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @GetMapping("{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        log.info("ClientController :: getClientById method called with id {}", id);
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        log.info("ClientController :: getClients method called");
        return ResponseEntity.ok(clientService.getClients());
    }

    @PostMapping("/send-message")
    public ResponseEntity<Void> sendMessageToClient(@RequestBody MessageToClient message) {
        log.info("ClientController :: sendMessageToClient method called with message {}", message);
        clientService.sendMessageToClient(message);
        return ResponseEntity.ok().build();
    }

}
