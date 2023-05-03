package kz.aparking.authservice.services;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.verify.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final NexmoClient nexmoClient;

    @Autowired
    public AuthenticationServiceImpl(NexmoClient nexmoClient) {
        this.nexmoClient = nexmoClient;
    }

    @Override
    public String requestVerificationCode(String phoneNumber) throws IOException, NexmoClientException {
        VerifyRequest request = new VerifyRequest(phoneNumber, "YourAppName");
        VerifyResponse response = nexmoClient.getVerifyClient().verify(request);
        if (response.getStatus() == VerifyStatus.OK) {
            return response.getRequestId();
        } else {
            throw new RuntimeException("Failed to send verification code: " + response.getErrorText());
        }
    }

    @Override
    public boolean verifyCode(String requestId, String code) {
        try {
            CheckResponse response = nexmoClient.getVerifyClient().check(requestId, code);
            return response.getStatus() == VerifyStatus.OK;
        } catch (IOException | NexmoClientException e) {
            throw new RuntimeException("Failed to verify code: " + e.getMessage(), e);
        }
    }
}