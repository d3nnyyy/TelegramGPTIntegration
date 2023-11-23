package ua.dtsebulia.telegramgptintegration.telegram.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.ChatLog;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    List<ChatLog> findAllByClientId(Long id);
}
