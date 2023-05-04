package kz.aparking.authservice.services;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.verify.*;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.user.User;
import kz.aparking.authservice.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final NexmoClient nexmoClient;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
    @Override
    public String register(User user, String code) {
        if (userService.existsByPhoneNumber(user.getPhone())) {
            throw new RuntimeException("User with this phone number already exists");
        }
        if (verifyCode(user.getPhone(), code)) {
            User newUser = userService.createUser(user);
            return jwtTokenUtil.generateToken(newUser.getPhone());
        } else {
            throw new RuntimeException("Verification code is incorrect");
        }
    }

    @Override
    public String login(String phoneNumber, String code) {
        User user = userService.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new RuntimeException("User with this phone number does not exist");
        }
        if (verifyCode(phoneNumber, code)) {
            return jwtTokenUtil.generateToken(user.getPhone());
        } else {
            throw new RuntimeException("Verification code is incorrect");
        }
    }
}