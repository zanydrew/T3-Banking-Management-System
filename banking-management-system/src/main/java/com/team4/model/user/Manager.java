package com.team4.model.user;

public class Manager extends User {

    public Manager(int id, String username, String password,
                   String fullname, String phone, String dOB) {
        super(id, username, password, fullname, phone, dOB, UserRole.MANAGER);
    }

    public Manager(int id, String username, String password,
                   String fullname, String phone, String dOB, boolean trusted) {
        super(id, username, password, fullname, phone, dOB, UserRole.MANAGER, trusted);
    }

    @Override
    public String toString() {
        return "User ID: " + getUserId() + " \nusername: " + getUsername() + "\nFull Name: "
                + getFullname() + "\nPhone Number: " + getPhone() + "\nDate of Birth: " + getdOB() + "\nUser Role: "
                + getRole();
    }
}
