package ua.dtsebulia.telegramgptintegration.telegram.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dtsebulia.telegramgptintegration.telegram.client.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
