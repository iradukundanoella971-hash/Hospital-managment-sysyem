package org.example.hospitalmanagmentsystem.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.hospitalmanagmentsystem.backend.Role;
import org.example.hospitalmanagmentsystem.backend.UserAccount;
import org.example.hospitalmanagmentsystem.component.AlertUtil;
import org.example.hospitalmanagmentsystem.component.HospitalContext;
import org.example.hospitalmanagmentsystem.component.SessionManager;
import org.example.hospitalmanagmentsystem.ui.SceneNavigator;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label feedbackLabel;

    @FXML
    private void handleLogin() {
        try {
            UserAccount user = HospitalContext.getInstance()
                    .getAuthService()
                    .login(usernameField.getText(), passwordField.getText());
            SessionManager.getInstance().login(user);

            Platform.runLater(() -> {
                if (user.getRole() == Role.ADMIN) {
                    SceneNavigator.navigate("/fxml/AdminDashboard.fxml", "Admin Dashboard");
                } else if (user.getRole() == Role.DOCTOR) {
                    SceneNavigator.navigate("/fxml/DoctorDashboard.fxml", "Doctor Dashboard");
                } else {
                    SceneNavigator.navigate("/fxml/PatientDashboard.fxml", "Patient Dashboard");
                }
            });
        } catch (Exception e) {
            AlertUtil.showError(feedbackLabel, e.getMessage());
        }
    }

    @FXML
    private void handleGoRegister() {
        SceneNavigator.navigate("/fxml/PatientDashboard.fxml", "Patient Dashboard");
    }
}
