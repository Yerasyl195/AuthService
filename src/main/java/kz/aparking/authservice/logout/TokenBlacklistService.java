package kz.aparking.authservice.logout;

public interface TokenBlacklistService {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}

