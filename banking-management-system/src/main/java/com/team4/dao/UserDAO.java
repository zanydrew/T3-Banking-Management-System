package com.team4.dao;

import com.team4.model.user.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User findUserById(int userId) throws SQLException;
    User findUserByUsername(String username) throws SQLException;
    List<User> findAllUsers() throws SQLException;
    void updateUserPhone(int userId, String newPhone) throws SQLException;
    void updateUserFullName(int userId, String newFullName) throws SQLException;
    void deleteUser(int userId) throws SQLException;         // manager only
    void createCustomer(User user) throws SQLException;
}
