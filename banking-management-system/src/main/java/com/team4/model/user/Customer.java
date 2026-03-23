package com.team4.model.user;

public class Customer extends User {
    public Customer(int userId, String username, String password, String fullname, String phone, String dOB,
            UserRole role) {
        super(userId, username, password, fullname, phone, dOB, UserRole.CUSTOMER);
    }
}
