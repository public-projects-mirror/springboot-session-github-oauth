package com.example.manager;

import com.example.entity.SessionDO;
import com.example.service.SessionService;

import java.util.UUID;

public class SessionAuthManager implements BaseAuthManager {

    SessionService sessionService;

    public SessionAuthManager(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String createSession(String userId) {
        UUID uuid = UUID.randomUUID();
        String sessionId = uuid.toString();
        saveSession(sessionId, userId);
        return sessionId;
    }

    @Override
    public void saveSession(String sessionId, String userId) {
        SessionDO sessionDO = new SessionDO();
        sessionDO.setSessionId(sessionId);
        sessionDO.setUserId(userId);
        sessionService.saveSession(sessionDO);
    }

    @Override
    public String getUserId(String sessionId) {
        SessionDO sessionDO = sessionService.getSessionBySessionId(sessionId);
        return sessionDO.getUserId();
    }

    @Override
    public void deleteSessionBySessionId(String sessionId) {
        SessionDO sessionDO = sessionService.getSessionBySessionId(sessionId);
        sessionService.deleteSession(sessionDO);
    }

}
