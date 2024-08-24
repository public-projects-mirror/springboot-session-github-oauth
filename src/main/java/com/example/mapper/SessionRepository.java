package com.example.mapper;

import com.example.entity.SessionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionDO, String> {
    SessionDO findBySessionId(String sessionId);
}
