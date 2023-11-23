package ua.dtsebulia.telegramgptintegration.user.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.dtsebulia.telegramgptintegration.user.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

}
