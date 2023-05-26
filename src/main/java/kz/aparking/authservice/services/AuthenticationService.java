package kz.aparking.authservice.services;


import kz.aparking.authservice.VerificationStatusAndToken;
import kz.aparking.authservice.user.User;

import java.io.IOException;

public interface AuthenticationService {
    //String requestVerificationCode(String phoneNumber) throws IOException, NexmoClientException;
    //boolean verifyCode(String phoneNumber, String code);
    String register(User user);
    //String login(String phoneNumber, String code);
    void requestVerificationCode(String phoneNumber);
    VerificationStatusAndToken authenticate(String phoneNumber, String code);
}
