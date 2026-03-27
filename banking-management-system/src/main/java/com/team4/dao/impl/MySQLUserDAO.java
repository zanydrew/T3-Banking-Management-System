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
        int      userId   = rs.getInt("user_id");
        String   username = rs.getString("username");
        String   password = rs.getString("password");
        String   fullname = rs.getString("full_name");
        String   phone    = rs.getString("phone");
        String   dob      = rs.getString("dob");
        UserRole role     = UserRole.valueOf(rs.getString("role"));

        // true = trusted DB load, skips all validation
        if (role == UserRole.MANAGER)
            return new Manager(userId, username, password, fullname, phone, dob, true);
        else
            return new Customer(userId, username, password, fullname, phone, dob,true);
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
    public void createUser(String username, String password, String fullName, String phone, String dob, UserRole role) throws SQLException {
        String sql = "INSERT INTO users(username, password, full_name, phone, dob, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, phone);
            stmt.setString(5, dob);
            stmt.setString(6, role.name());
            stmt.executeUpdate();
        }
    }
}

