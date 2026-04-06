package com.team4.model.user;

public class Customer extends User {
    // Used for NEW customer creation (validated)
    public Customer(int id, String username, String password,
                    String fullname, String phone, String dOB, UserRole customer) {
        super(id, username, password, fullname, phone, dOB, UserRole.CUSTOMER);
    }

    // Used for DB (trusted data, no validation)
    public Customer(int id, String username, String password,
                    String fullname, String phone, String dOB, boolean trusted) {
        super(id, username, password, fullname, phone, dOB, UserRole.CUSTOMER, trusted);
    }

    @Override
    public String toString() {
        return "User ID: " + getUserId() + " \nusername: " + getUsername() + "\nFull Name: "
                + getFullname() + "\nPhone Number: " + getPhone() + "\nDate of Birth: " + getdOB() + "\nUser Role: "
                + getRole();
    }
}
