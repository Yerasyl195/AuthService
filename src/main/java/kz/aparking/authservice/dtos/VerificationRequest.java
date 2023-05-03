package kz.aparking.authservice.dtos;

public class VerificationRequest {
    private String requestId;
    private String code;
    private String phoneNumber;

    public VerificationRequest() {
    }

    public VerificationRequest(String requestId, String code) {
        this.requestId = requestId;
        this.code = code;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
