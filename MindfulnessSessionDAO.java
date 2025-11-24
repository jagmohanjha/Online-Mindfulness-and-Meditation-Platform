package com.guvi.mindfulness.dao;

import com.guvi.mindfulness.jdbc.DBConnection;
import com.guvi.mindfulness.model.MindfulnessSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for the {@code mindfulness_sessions} table.
 */
public class MindfulnessSessionDAO {

    private static final String INSERT_SQL = """
            INSERT INTO mindfulness_sessions(user_id, title, description, difficulty, category, scheduled_at, duration_minutes, reflection_notes)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_BY_ID_SQL = """
            SELECT id, user_id, title, description, difficulty, category, scheduled_at, duration_minutes, reflection_notes
            FROM mindfulness_sessions WHERE id = ?
            """;

    private static final String SELECT_BY_USER_SQL = """
            SELECT id, user_id, title, description, difficulty, category, scheduled_at, duration_minutes, reflection_notes
            FROM mindfulness_sessions
            WHERE user_id = ?
            ORDER BY scheduled_at DESC
            """;

    private static final String UPDATE_NOTES_SQL = """
            UPDATE mindfulness_sessions
            SET reflection_notes = ?, duration_minutes = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = "DELETE FROM mindfulness_sessions WHERE id = ?";

    public long insert(MindfulnessSession session) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, session.getUserId());
            statement.setString(2, session.getTitle());
            statement.setString(3, session.getDescription());
            statement.setString(4, session.getDifficulty());
            statement.setString(5, session.getCategory());
            statement.setTimestamp(6, Timestamp.valueOf(session.getScheduledAt()));
            statement.setInt(7, session.getDurationMinutes());
            statement.setString(8, session.getReflectionNotes());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        return -1;
    }

    public MindfulnessSession findById(long id) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<MindfulnessSession> findByUser(long userId) throws SQLException {
        List<MindfulnessSession> sessions = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_USER_SQL)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapRow(rs));
                }
            }
        }
        return sessions;
    }

    public boolean updateReflection(long sessionId, String notes, int durationMinutes) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_NOTES_SQL)) {
            statement.setString(1, notes);
            statement.setInt(2, durationMinutes);
            statement.setLong(3, sessionId);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean delete(long sessionId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, sessionId);
            return statement.executeUpdate() == 1;
        }
    }

    private MindfulnessSession mapRow(ResultSet rs) throws SQLException {
        MindfulnessSession session = new MindfulnessSession();
        session.setId(rs.getLong("id"));
        session.setUserId(rs.getLong("user_id"));
        session.setTitle(rs.getString("title"));
        session.setDescription(rs.getString("description"));
        session.setDifficulty(rs.getString("difficulty"));
        session.setCategory(rs.getString("category"));
        Timestamp scheduled = rs.getTimestamp("scheduled_at");
        session.setScheduledAt(scheduled != null ? scheduled.toLocalDateTime() : LocalDateTime.now());
        session.setDurationMinutes(rs.getInt("duration_minutes"));
        session.setReflectionNotes(rs.getString("reflection_notes"));
        return session;
    }
}

