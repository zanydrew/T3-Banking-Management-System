package com.team4.dao.impl;


import com.team4.dao.SessionDAO;
import com.team4.model.user.UserRole;
import com.team4.session.Session;

import java.sql.*;
import java.time.LocalDateTime;

public class MySQLSessionDAO implements SessionDAO {

    private final Connection connection;

    public MySQLSessionDAO(Connection connection) {
        this.connection = connection;
    }

    // == Helper ==
    private Session mapRow(ResultSet rs) throws SQLException {
        return new Session(
                rs.getString("session_id"),
                rs.getInt("user_id"),
                rs.getString("username"),
                UserRole.valueOf(rs.getString("role")),
                rs.getTimestamp("login_at").toLocalDateTime(),
                rs.getTimestamp("last_active_at").toLocalDateTime(),
                rs.getBoolean("is_active")
        );
    }

    @Override
    public void createSession(Session session) throws SQLException {
        String sql = "INSERT INTO sessions(session_id, user_id, username, role, login_at, last_active_at, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, session.getSessionId());
            stmt.setInt(2, session.getUserId());
            stmt.setString(3, session.getUsername());
            stmt.setString(4, session.getRole().name());
            stmt.setTimestamp(5, Timestamp.valueOf(session.getLoginAt()));
            stmt.setTimestamp(6, Timestamp.valueOf(session.getLastActiveAt()));
            stmt.setBoolean(7, session.isActive());
            stmt.executeUpdate();
        }
    }

    @Override
    public Session findActiveSessionByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE user_id = ? AND is_active = TRUE ORDER BY login_at DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public void updateLastActive(String sessionId) throws SQLException {
        String sql = "UPDATE sessions SET last_active_at = ? WHERE session_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, sessionId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deactivateSession(String sessionId) throws SQLException {
        String sql = "UPDATE sessions SET is_active = FALSE WHERE session_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sessionId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deactivateAllSessionsByUserId(int userId) throws SQLException {
        String sql = "UPDATE sessions SET is_active = FALSE WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

}

