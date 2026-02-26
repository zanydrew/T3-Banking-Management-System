package com.team4.model.user;

public class Manager extends User {

    protected Manager(String id, String username, String password, String fullname, String phone, String dOB,
            String role) {
        super(id, username, password, fullname, phone, dOB, UserRole.MANAGER);
    }
}
