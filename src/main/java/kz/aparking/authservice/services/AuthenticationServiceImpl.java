package kz.aparking.authservice.services;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import kz.aparking.authservice.VerificationStatusAndToken;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.user.User;
import kz.aparking.authservice.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    //private final NexmoClient nexmoClient;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private final String TWILIO_ACCOUNT_SID = "AC9203b7c1e4cd2817039de0e65f40b10d";
    private final String TWILIO_AUTH_TOKEN = "f6cf6652bd3e2e6a2e297793b95cd783";
    private final String SERVICE_ID = "VA91e3cf08dfb9ed7d80c8ade6cad6ad66";

    public AuthenticationServiceImpl() {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
    }


    @Override
    public String register(User user) {
        if (userService.existsByPhone(user.getPhone())) {
            throw new RuntimeException("User with this phone number already exists");
        }
        User newUser = userService.createUser(user);
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

//    @Override
//    public String login(String requestId, String code) {
//        User user = userService.findByPhone(requestId);
//        if (user == null) {
//            throw new RuntimeException("User with this phone number does not exist");
//        }
//        if (verifyCode(requestId, code)) {
//            return jwtTokenUtil.generateToken(user.getPhone());
//        } else {
//            throw new RuntimeException("Verification code is incorrect");
//        }
//    }

//    @Autowired
//    public AuthenticationServiceImpl(NexmoClient nexmoClient) {
//        this.nexmoClient = nexmoClient;
//    }

//    @Override
//    public String requestVerificationCode(String phoneNumber) throws IOException, NexmoClientException {
//        VerifyRequest request = new VerifyRequest(phoneNumber, "AParking");
//        VerifyResponse response = nexmoClient.getVerifyClient().verify(request);
//        if (response.getStatus() == VerifyStatus.OK) {
//            return response.getRequestId();
//        } else {
//            throw new RuntimeException("Failed to send verification code: " + response.getErrorText());
//        }
//    }
//
//    @Override
//    public boolean verifyCode(String requestId, String code) {
//        try {
//            CheckResponse response = nexmoClient.getVerifyClient().check(requestId, code);
//            return response.getStatus() == VerifyStatus.OK;
//        } catch (IOException | NexmoClientException e) {
//            throw new RuntimeException("Failed to verify code: " + e.getMessage(), e);
//        }
//    }