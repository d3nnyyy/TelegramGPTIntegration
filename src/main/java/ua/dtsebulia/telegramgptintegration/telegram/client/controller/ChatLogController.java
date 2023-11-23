package ua.dtsebulia.telegramgptintegration.telegram.client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.ChatLog;
import ua.dtsebulia.telegramgptintegration.telegram.client.service.ChatLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatlogs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatLogController {

    private final ChatLogService chatLogService;

    @GetMapping
    public ResponseEntity<List<ChatLog>> getChatLogs() {
        return ResponseEntity.ok(chatLogService.getChatLogs());
    }

    @GetMapping("{id}")
    public ResponseEntity<List<ChatLog>> getChatLogsByClientId(@PathVariable String id) {
        return ResponseEntity.ok(chatLogService.getChatLogsByClientId(id));
    }
}

