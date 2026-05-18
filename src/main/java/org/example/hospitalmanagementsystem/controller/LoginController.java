package org.example.hospitalmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hospitalmanagementsystem.database.HospitalDatabase;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label         messageLabel;
    @FXML private Label         titleLabel;

    private String role;

    public void setRole(String role) {
        this.role = role;
        titleLabel.setText(capitalize(role) + " Login");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username and password.");
            return;
        }

        try {
            boolean valid = switch (role) {
                case "admin"   -> HospitalDatabase.loginAdmin(username, password);
                case "doctor"  -> HospitalDatabase.loginDoctor(username, password);
                default        -> HospitalDatabase.loginPatient(username, password);
            };

            if (valid) {
                openDashboard(username);
            } else {
                messageLabel.setText("Invalid username or password.");
            }

        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }

    private void openDashboard(String username) {
        try {
            String fxmlFile = switch (role) {
                case "admin"  -> "/org/example/hospitalmanagementsystem/fxml/adminDashboard.fxml";
                case "doctor" -> "/org/example/hospitalmanagementsystem/fxml/doctorDashboard.fxml";
                default       -> "/org/example/hospitalmanagementsystem/fxml/patientDashboard.fxml";
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load(), 1100, 700);

            switch (role) {
                case "admin"  -> ((AdminController)   loader.getController()).setUsername(username);
                case "doctor" -> ((DoctorController)  loader.getController()).setUsername(username);
                default       -> ((PatientController) loader.getController()).setUsername(username);
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            messageLabel.setText("Error loading dashboard.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/org/example/hospitalmanagementsystem/fxml/home.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 1100, 700));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
