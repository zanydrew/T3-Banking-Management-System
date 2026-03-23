package com.team4.service;

import com.team4.dao.SessionDAO;
import com.team4.dao.UserDAO;
import com.team4.model.user.User;
import com.team4.model.user.UserRole;
import com.team4.session.Session;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class AuthService {

    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private final Connection connection;

    private Session currentSession;

    public AuthService(UserDAO userDAO, SessionDAO sessionDAO, Connection connection) {
        this.userDAO    = userDAO;
        this.sessionDAO = sessionDAO;
        this.connection = connection;
    }

    // Helpers

    private String getUserPassword(String username) throws SQLException {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("password");
        }
        throw new IllegalArgumentException("User not found.");
    }

    public Session getCurrentSession()  { return currentSession; }
    public boolean isLoggedIn()         { return currentSession != null && currentSession.isActive(); }

    public void refreshSession() {
        if (currentSession == null) return;
        try {
            sessionDAO.updateLastActive(currentSession.getSessionId());
            currentSession.setLastActiveAt(LocalDateTime.now());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void requireLogin() {
        if (!isLoggedIn())
            throw new IllegalStateException("You must be logged in.");
    }

    public void requireRole(UserRole required) {
        requireLogin();
        if (currentSession.getRole() != required)
            throw new IllegalStateException("Access denied. Required role: " + required);
    }

    // login

    public Session login(String username, String plainPassword) {
        try {
            // Find user
            User user = userDAO.findUserByUsername(username);
            if (user == null)
                throw new IllegalArgumentException("Invalid username or password.");

            // Compare plain text directly
            String storedPassword = getUserPassword(username);
            if (!storedPassword.equals(plainPassword))
                throw new IllegalArgumentException("Invalid username or password.");

            // Invalidate any previous sessions
            connection.setAutoCommit(false);
            sessionDAO.deactivateAllSessionsByUserId(user.getUserId());

            // Create new session
            Session session = new Session(
                    UUID.randomUUID().toString(),
                    user.getUserId(),
                    user.getUsername(),
                    user.getRole(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    true
            );
            sessionDAO.createSession(session);
            connection.commit();

            this.currentSession = session;
            System.out.println("Login successful: " + session);
            return session;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Login failed due to database error.", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // logout

    public void logout() {
        if (currentSession == null)
            throw new IllegalStateException("No active session to log out.");

        try {
            connection.setAutoCommit(false);
            sessionDAO.deactivateSession(currentSession.getSessionId());
            connection.commit();

            System.out.println("Logged out: " + currentSession.getUsername());
            currentSession = null;

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Logout failed.", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
