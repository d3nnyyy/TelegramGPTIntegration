package ua.dtsebulia.telegramgptintegration.telegram.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.ChatLog;
import ua.dtsebulia.telegramgptintegration.telegram.client.service.ChatLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatlogs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ChatLogController {

    private final ChatLogService chatLogService;

    @GetMapping
    public ResponseEntity<List<ChatLog>> getChatLogs() {
        log.info("ChatLogController :: getChatLogs method called");
        return ResponseEntity.ok(chatLogService.getChatLogs());
    }

    @GetMapping("{id}")
    public ResponseEntity<List<ChatLog>> getChatLogsByClientId(@PathVariable String id) {
        log.info("ChatLogController :: getChatLogsByClientId method called with id {}", id);
        return ResponseEntity.ok(chatLogService.getChatLogsByClientId(id));
    }
}

