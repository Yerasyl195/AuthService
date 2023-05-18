package kz.aparking.authservice.services;

import kz.aparking.authservice.services.TokenBlacklistService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final Set<String> blacklistedTokens = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void addToBlacklist(String token) {
        if (token != null) {
            blacklistedTokens.add(token);
        }
    }


    @Override
    public boolean isBlacklisted(String token) {
        if (token == null) {
            return false;
        }
        return blacklistedTokens.contains(token);
    }

}

