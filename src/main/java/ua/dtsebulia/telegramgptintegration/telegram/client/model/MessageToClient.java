package ua.dtsebulia.telegramgptintegration.telegram.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageToClient {
    private long clientId;
    private String message;
}
