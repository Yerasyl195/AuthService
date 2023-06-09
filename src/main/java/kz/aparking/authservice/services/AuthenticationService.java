package kz.aparking.authservice.services;

import com.nexmo.client.NexmoClientException;
import kz.aparking.authservice.user.models.User;

import java.io.IOException;

public interface AuthenticationService {
    String requestVerificationCode(String phoneNumber) throws IOException, NexmoClientException;
    boolean verifyCode(String phoneNumber, String code);
    String register(User user);
    String login(String phoneNumber, String code);
}
