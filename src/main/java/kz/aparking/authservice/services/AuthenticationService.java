package kz.aparking.authservice.services;

import com.nexmo.client.NexmoClientException;
import kz.aparking.authservice.dtos.RegistrationRequest;
import kz.aparking.authservice.models.User;
import kz.aparking.authservice.models.VerificationStatusAndToken;

import java.io.IOException;

public interface AuthenticationService {
    String register(RegistrationRequest userDto);
    void requestVerificationCode(String phoneNumber);
    VerificationStatusAndToken authenticate(String phoneNumber, String code);
}
