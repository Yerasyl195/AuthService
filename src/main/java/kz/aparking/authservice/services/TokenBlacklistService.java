package kz.aparking.authservice.services;

import org.springframework.stereotype.Service;

@Service
public interface TokenBlacklistService {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}

