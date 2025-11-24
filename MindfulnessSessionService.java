package com.guvi.mindfulness.service;

import com.guvi.mindfulness.dao.MindfulnessSessionDAO;
import com.guvi.mindfulness.exception.DataAccessException;
import com.guvi.mindfulness.exception.ValidationException;
import com.guvi.mindfulness.model.MindfulnessSession;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service orchestrating mindfulness session logic.
 */
public class MindfulnessSessionService {

    private final MindfulnessSessionDAO sessionDAO;

    public MindfulnessSessionService(MindfulnessSessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public long scheduleSession(MindfulnessSession session) throws ValidationException {
        validateSession(session);
        try {
            return sessionDAO.insert(session);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to schedule session", e);
        }
    }

    public List<MindfulnessSession> sessionsForUser(long userId) {
        try {
            return sessionDAO.findByUser(userId);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch sessions", e);
        }
    }

    public boolean updateReflection(long sessionId, String notes, int durationMinutes) throws ValidationException {
        if (sessionId <= 0) {
            throw new ValidationException("Session id is required");
        }
        if (durationMinutes <= 0) {
            throw new ValidationException("Duration must be greater than zero");
        }
        try {
            return sessionDAO.updateReflection(sessionId, notes, durationMinutes);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update session", e);
        }
    }

    public boolean delete(long sessionId) {
        try {
            return sessionDAO.delete(sessionId);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete session", e);
        }
    }

    private void validateSession(MindfulnessSession session) throws ValidationException {
        if (session == null) {
            throw new ValidationException("Session payload cannot be null");
        }
        if (session.getUserId() <= 0) {
            throw new ValidationException("Session must belong to a user");
        }
        if (session.getTitle() == null || session.getTitle().isBlank()) {
            throw new ValidationException("Session title is required");
        }
        if (session.getScheduledAt() == null || session.getScheduledAt().isBefore(LocalDateTime.now().minusDays(1))) {
            throw new ValidationException("Session date looks incorrect");
        }
        if (session.getDurationMinutes() <= 0) {
            throw new ValidationException("Duration must be positive");
        }
    }
}

