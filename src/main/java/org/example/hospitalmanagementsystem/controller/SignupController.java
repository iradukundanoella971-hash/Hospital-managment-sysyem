package org.example.hospitalmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hospitalmanagementsystem.backend.Patient;
import org.example.hospitalmanagementsystem.database.HospitalDatabase;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController {

    @FXML private TextField     patientIdField;
    @FXML private TextField     nameField;
    @FXML private TextField     locationField;
    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label         messageLabel;
    @FXML
    private void handleSignup() {
        String patientId = patientIdField.getText().trim();
        String name      = nameField.getText().trim();
        String location  = locationField.getText().trim();
        String username  = usernameField.getText().trim();
        String password  = passwordField.getText().trim();

        if (patientId.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all required fields.");
            return;
        }

        try {
            HospitalDatabase.insertPatient(new Patient(patientId, name, location, username, password));
            messageLabel.setStyle("-fx-text-fill: #27ae60;");
            messageLabel.setText("Account created! You can now login.");
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique"))
                messageLabel.setText("Patient ID or Username already exists.");
            else
                messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/org/example/hospitalmanagementsystem/fxml/home.fxml"));
            Stage stage = (Stage) patientIdField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 1100, 700));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
