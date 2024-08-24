package com.example.manager;

public interface BaseAuthManager {
    void saveSession(String sessionId, String userId);
    String createSession(String userId);
    String getUserId(String sessionId);
    void deleteSessionBySessionId(String sessionId);
}
