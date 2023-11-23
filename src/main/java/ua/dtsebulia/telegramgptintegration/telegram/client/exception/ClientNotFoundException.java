package ua.dtsebulia.telegramgptintegration.telegram.client.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String s) {
        super(s);
    }
}
