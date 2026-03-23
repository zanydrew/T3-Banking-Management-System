package com.team4.session;


import com.team4.model.user.UserRole;

import java.time.LocalDateTime;

public class Session {
    private final String sessionId;
    private final int userId;
    private final String username;
    private final UserRole role;
    private final LocalDateTime loginAt;
    private LocalDateTime lastActiveAt;
    private boolean active;

    public Session(String sessionId, int userId, String username,
                   UserRole role, LocalDateTime loginAt, LocalDateTime lastActiveAt,
                   boolean active) {
        this.sessionId      = sessionId;
        this.userId         = userId;
        this.username       = username;
        this.role           = role;
        this.loginAt        = loginAt;
        this.lastActiveAt   = lastActiveAt;
        this.active         = active;
    }

    public String getSessionId()          { return sessionId; }
    public int getUserId()                { return userId; }
    public String getUsername()           { return username; }
    public UserRole getRole()                 { return role; }
    public LocalDateTime getLoginAt()     { return loginAt; }
    public LocalDateTime getLastActiveAt(){ return lastActiveAt; }
    public boolean isActive()             { return active; }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isManager()  { return role == UserRole.MANAGER; }
    public boolean isCustomer() { return role == UserRole.CUSTOMER; }

    @Override
    public String toString() {
        return String.format("Session[%s] user=%s role=%s login=%s active=%s",
                sessionId.substring(0, 8), username, role, loginAt, active);
    }
}

