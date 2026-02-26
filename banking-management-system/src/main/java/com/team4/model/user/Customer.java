package com.team4.model;

public class Customer extends User {
    protected Customer(String id, String username, String password, String fullname, String phone, String dOB,
            String role) {
        super(id, username, password, fullname, phone, dOB, UserRole.CUSTOMER);
    }
}
