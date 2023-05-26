package kz.aparking.authservice;

import kz.aparking.authservice.dtos.AuthenticationRequest;
import kz.aparking.authservice.dtos.VerificationRequest;
import kz.aparking.authservice.services.AuthenticationService;
import kz.aparking.authservice.user.User;
import kz.aparking.authservice.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

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

//    @PostMapping("/request")
//    public ResponseEntity<String> requestCode(@RequestBody AuthenticationRequest authRequest) throws IOException {
//        try {
//            String requestId = authenticationService.requestVerificationCode(authRequest.getPhoneNumber());
//            return ResponseEntity.ok(requestId);
//        } catch (RuntimeException | NexmoClientException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

//    @PostMapping("/verify")
//    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequest verificationRequest) {
//        try {
//            boolean isVerified = authenticationService.verifyCode(verificationRequest.getRequestId(), verificationRequest.getCode());
//            if (isVerified) {
//                return ResponseEntity.ok(verificationRequest.getPhoneNumber());
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//            }
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            String jwtToken = authenticationService.register(user);
            return ResponseEntity.ok(jwtToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/request")
    public ResponseEntity<String> requestCode(@RequestBody AuthenticationRequest authRequest) {
        try {
            authenticationService.requestVerificationCode(authRequest.getPhoneNumber());
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody VerificationRequest verificationRequest) {
        try {
            VerificationStatusAndToken statusAndToken = authenticationService.authenticate(
                    verificationRequest.getPhoneNumber(),
                    verificationRequest.getCode()
            );
            if (statusAndToken.getStatus().equals("registration")) {
                return ResponseEntity.ok(Map.of("status", "registration", "phoneNumber", statusAndToken.getPhoneNumber(), "token", statusAndToken.getToken()));
            } else {
                return ResponseEntity.ok(Map.of("status", "login", "token", statusAndToken.getToken()));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody VerificationRequest verificationRequest) {
//        try {
//            String jwtToken = authenticationService.login(verificationRequest.getRequestId(), verificationRequest.getCode());
//            return ResponseEntity.ok(jwtToken);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }
}
