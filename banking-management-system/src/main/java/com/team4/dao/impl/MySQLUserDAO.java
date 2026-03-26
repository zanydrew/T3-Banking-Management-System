package com.team4.dao.impl;


import com.team4.dao.UserDAO;
import com.team4.model.user.Customer;
import com.team4.model.user.Manager;
import com.team4.model.user.User;
import com.team4.model.user.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserDAO implements UserDAO {
    private final Connection connection;

    public MySQLUserDAO(Connection connection) {
        this.connection = connection;
    }

    // create mapRow() method for reusable in extract data from the row in DB

    private User mapRow(ResultSet rs) throws SQLException {
        // make sure row found in DB
        System.out.println("Row found in DB!");
        int user_id = rs.getInt("user_id");
        String user_name = rs.getString("username");
        String password = rs.getString("password");
        String fullName = rs.getString("full_name");
        String phone = rs.getString("phone");
        String dOB = rs.getString("dOB");
        String role = rs.getString("role");
        if(role.equalsIgnoreCase("MANAGER")){
            return new Manager(user_id, user_name, password, fullName, phone, dOB, UserRole.valueOf(role));
        }
        else if(role.equalsIgnoreCase("CUSTOMER")){
            return new Customer(user_id, user_name, password, fullName, phone, dOB, UserRole.valueOf(role));
        }
        return null;
    }

    @Override
    public User findUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<User> findAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) users.add(mapRow(rs));
        }
        return users;
    }

    @Override
    public void updateUserPhone(int userId, String newPhone) throws SQLException {
        String sql = "UPDATE users SET phone = ? WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPhone);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateUserFullName(int userId, String newFullName) throws SQLException {
        String sql = "UPDATE users SET full_name = ? WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newFullName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void createCustomer(User user) throws SQLException {
        String sql = """
        INSERT INTO users (username, password, full_name, phone, dOB, role)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullname());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getdOB());
            stmt.setString(6, user.getRole().name());

            stmt.executeUpdate();
        }
    }
}

