package kz.aparking.authservice.services;

import com.nexmo.client.NexmoClientException;

import java.io.IOException;

public interface AuthenticationService {
    String requestVerificationCode(String phoneNumber) throws IOException, NexmoClientException;
    boolean verifyCode(String phoneNumber, String code);
}
