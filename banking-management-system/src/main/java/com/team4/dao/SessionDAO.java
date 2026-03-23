package com.team4.dao;

import com.team4.session.Session;
import java.sql.SQLException;

public interface SessionDAO {
    void createSession(Session session) throws SQLException;
    Session findActiveSessionByUserId(int userId) throws SQLException;
    void updateLastActive(String sessionId) throws SQLException;
    void deactivateSession(String sessionId) throws SQLException;
    void deactivateAllSessionsByUserId(int userId) throws SQLException;
}
