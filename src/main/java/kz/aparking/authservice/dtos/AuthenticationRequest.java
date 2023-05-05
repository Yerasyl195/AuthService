package kz.aparking.authservice.dtos;

import kz.aparking.authservice.user.User;

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
