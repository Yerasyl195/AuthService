package kz.aparking.authservice.dtos;

public class VerificationRequest {
    private String code;
    private String phoneNumber;

    public VerificationRequest() {
    }

    public VerificationRequest(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
