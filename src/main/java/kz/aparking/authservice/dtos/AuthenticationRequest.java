package kz.aparking.authservice.dtos;

public class AuthenticationRequest {
    private String phoneNumber;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
