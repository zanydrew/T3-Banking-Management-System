package com.team4.controller;


import com.team4.app.App;
import com.team4.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    //    @FXML private CheckBox rememberMe;
    @FXML private Label         errorLabel;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        // Clear error when user starts typing
        usernameField.textProperty().addListener((obs, o, n) -> hideError());
        passwordField.textProperty().addListener((obs, o, n) -> hideError());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // --- Validation ---
        if (username.isEmpty() && password.isEmpty()) {
            showError("Please enter your username and password.");
            usernameField.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            showError("Username cannot be empty.");
            usernameField.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            showError("Password cannot be empty.");
            passwordField.requestFocus();
            return;
        }

        // --- Console output (temporary) ---
        System.out.println("=== Login Attempt ===");
        System.out.println("Username    : " + username);
//        System.out.println("Remember me : " + rememberMe.isSelected());
        System.out.println("=====================");

        // --- Disable button while processing ---
        loginButton.setDisable(true);
        loginButton.setText("Signing in...");

        try {
            // TODO: replace with real auth when ready
            Session session = App.authService.login(username, password);
            if (session.isManager())
                App.switchScene("manager-dashboard.fxml");
            else
                App.switchScene("customer-dashboard.fxml");

            // Temporary: simulate login
//            System.out.println("Login successful for: " + username);

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (RuntimeException e) {
            showError("System error. Please try again.");
            e.printStackTrace();
        } finally {
            loginButton.setDisable(false);
            loginButton.setText("Sign In");
        }

        Session session = App.authService.login(username, password);
//        MainApp.switchScene("manager-dashboard.fxml");

        if (session.isManager())
            App.switchScene("manager-dashboard.fxml");
        else
            App.switchScene("customer-dashboard.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

}