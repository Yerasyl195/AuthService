package kz.aparking.authservice.dtos;

import kz.aparking.authservice.user.User;

public class AuthenticationRequest {
    private User user;
    private String phoneNumber;
    private String code;

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

    public String getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
