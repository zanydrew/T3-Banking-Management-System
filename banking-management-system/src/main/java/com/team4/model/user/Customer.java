package com.team4.model.user;

public class Customer extends User {
    protected Customer(int userId, String username, String password, String fullname, String phone, String dOB,
            String role) {
        super(userId, username, password, fullname, phone, dOB, UserRole.CUSTOMER);
    }
}
