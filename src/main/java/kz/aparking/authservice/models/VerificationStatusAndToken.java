package kz.aparking.authservice.models;

public class VerificationStatusAndToken {
    private String status;
    private String phoneNumber;
    private String token;

    public VerificationStatusAndToken(){}

    public VerificationStatusAndToken(String status, String phoneNumber, String token){
        this.token = token;
        this.status = status;
        this.phoneNumber = phoneNumber;
    }

    public VerificationStatusAndToken(String status, String phoneNumber){
        this.status = status;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setToken(String token) {
        this.token = token;
    }
}