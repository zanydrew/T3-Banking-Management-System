package com.team4;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        try {
            var session = App.authService.login(username, password);

            if (session.isManager())
                App.switchScene("manager-dashboard.fxml");
            else
                App.switchScene("customer-dashboard.fxml");

        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        } catch (RuntimeException e) {
            errorLabel.setText("System error. Please try again.");
        }
    }
}