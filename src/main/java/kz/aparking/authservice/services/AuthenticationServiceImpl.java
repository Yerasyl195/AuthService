package kz.aparking.authservice.services;

import com.nexmo.client.NexmoClient;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import kz.aparking.authservice.dtos.RegistrationRequest;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.models.User;
import kz.aparking.authservice.models.VerificationStatusAndToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private final String TWILIO_ACCOUNT_SID = "AC9203b7c1e4cd2817039de0e65f40b10d";
    private final String TWILIO_AUTH_TOKEN = "f12ab7294bd0c804d2e18524197fe85e";
    private final String SERVICE_ID = "VA91e3cf08dfb9ed7d80c8ade6cad6ad66";


    public AuthenticationServiceImpl() {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
    }

    @Override
    public String register(RegistrationRequest userDto) {
        if (userService.existsByPhone(userDto.getPhone())) {
            throw new RuntimeException("User with this phone number already exists");
        }
        User newUser = userService.createUser(userDto);
        return jwtTokenUtil.generateToken(newUser.getPhone());
    }

    @Override
    public VerificationStatusAndToken authenticate(String phoneNumber, String code) {
        VerificationCheck verificationCheck = VerificationCheck
                .creator(SERVICE_ID)
                .setTo(phoneNumber)
                .setCode(code)
                .create();
        System.out.println(verificationCheck.getStatus());

        if (!verificationCheck.getStatus().equals("approved")) {
            throw new RuntimeException("Verification code is incorrect");
        }

        User user = userService.findByPhone(phoneNumber);
        if (user == null) {
            return new VerificationStatusAndToken("registration", phoneNumber);
        } else {
            String token = jwtTokenUtil.generateToken(phoneNumber);
            return new VerificationStatusAndToken("login", user.getPhone(), token);
        }
    }

    @Override
    public void requestVerificationCode(String phoneNumber) {
        Verification verification = Verification.creator(SERVICE_ID, phoneNumber, "whatsapp").create();
        System.out.println(verification.getSid());
        if (!verification.getStatus().equals("pending")) {
            throw new RuntimeException("Failed to send verification code: " + verification.getStatus());
        }
    }
}