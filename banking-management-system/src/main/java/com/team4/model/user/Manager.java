package com.team4.model.user;

public class Manager extends User {

    protected Manager(int userId, String username, String password, String fullname, String phone, String dOB,
            String role) {
        super(userId, username, password, fullname, phone, dOB, UserRole.MANAGER);
    }
}
