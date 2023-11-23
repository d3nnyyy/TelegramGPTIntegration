package ua.dtsebulia.telegramgptintegration.user.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.dtsebulia.telegramgptintegration.user.exception.ErrorResponse;
import ua.dtsebulia.telegramgptintegration.user.exception.UserAlreadyExistsException;
import ua.dtsebulia.telegramgptintegration.user.exception.UserNotFoundException;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request for user: {}", request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.registerUser(request);
            log.info("User registered successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            log.error("Registration failed - User already exists: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("User already exists", "The specified user already exists."));
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        log.info("Received registration request for admin: {}", request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.registerAdmin(request);
            log.info("Admin registered successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            log.error("Registration failed - Admin already exists: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("User already exists", "The specified user already exists."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        log.info("Received login request for user: {}", request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.login(request);
            log.info("User logged in successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            log.error("Login failed - User not found: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found", "The specified user does not exist."));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        log.info("Received refresh token request");
        try {
            authenticationService.refreshToken(request, response);
            log.info("Token refreshed successfully");
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("Token refresh failed");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Token refresh failed", "The token refresh failed."));
        }
    }

}
