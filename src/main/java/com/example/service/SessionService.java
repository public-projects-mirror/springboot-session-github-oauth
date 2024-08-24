package com.example.service;

import com.example.entity.SessionDO;
import com.example.exception.SessionNotFoundException;
import com.example.mapper.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Transactional
    public void SessionExists(SessionDO sessionDO) {
        if (sessionDO == null) {
            throw new SessionNotFoundException("Session not found");
        }
    }

    public void saveSession(SessionDO sessionDO) {
        sessionRepository.save(sessionDO);
    }

    public void deleteSession(SessionDO sessionDO) {
        sessionRepository.delete(sessionDO);
    }

    @Transactional
    public SessionDO getSessionBySessionId(String sessionId) {
        SessionDO sessionDO = sessionRepository.findBySessionId(sessionId);
        SessionExists(sessionDO);
        return sessionDO;
    }

}
