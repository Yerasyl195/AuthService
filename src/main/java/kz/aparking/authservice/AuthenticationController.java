package kz.aparking.authservice;

import com.nexmo.client.NexmoClientException;
import kz.aparking.authservice.dtos.AuthenticationRequest;
import kz.aparking.authservice.dtos.VerificationRequest;
import kz.aparking.authservice.services.AuthenticationService;
import kz.aparking.authservice.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestCode(@RequestBody AuthenticationRequest authRequest) throws IOException {
        try {
            String requestId = authenticationService.requestVerificationCode(authRequest.getPhoneNumber());
            return ResponseEntity.ok(requestId);
        } catch (RuntimeException | NexmoClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        try {
            boolean isVerified = authenticationService.verifyCode(verificationRequest.getRequestId(), verificationRequest.getCode());
            if (isVerified) {

                return ResponseEntity.ok(verificationRequest.getPhoneNumber());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthenticationRequest authRequest) {
        try {
            String jwtToken = authenticationService.register(authRequest.getUser(), authRequest.getCode());
            return ResponseEntity.ok(jwtToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest authRequest) {
        try {
            String jwtToken = authenticationService.login(authRequest.getPhoneNumber(), authRequest.getCode());
            return ResponseEntity.ok(jwtToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
