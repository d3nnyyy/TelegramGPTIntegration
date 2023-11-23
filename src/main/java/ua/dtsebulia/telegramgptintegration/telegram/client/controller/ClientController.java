package ua.dtsebulia.telegramgptintegration.telegram.client.controller;

import lombok.RequiredArgsConstructor;
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
public class ClientController {

    private final ClientService clientService;

    @GetMapping("{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @PostMapping("/send-message")
    public ResponseEntity<Void> sendMessageToClient(@RequestBody MessageToClient message) {
        clientService.sendMessageToClient(message);
        return ResponseEntity.ok().build();
    }

}
