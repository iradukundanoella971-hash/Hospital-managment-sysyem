package org.example.hospitalmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
public class HomeController {

    @FXML private Button adminBtn;
    @FXML private Button doctorBtn;
    @FXML private Button patientBtn;
    @FXML private Button signupBtn;

    @FXML
    private void handleAdminLogin() {
        openLogin("admin", adminBtn);
    }

    @FXML
    private void handleDoctorLogin() {
        openLogin("doctor", doctorBtn);
    }

    @FXML
    private void handlePatientLogin() {
        openLogin("patient", patientBtn);
    }

    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/org/example/hospitalmanagementsystem/fxml/signup.fxml"
                )
            );
            Scene scene = new Scene(loader.load(), 1100, 700);
            Stage stage = (Stage) signupBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openLogin(String role, Button source) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/org/example/hospitalmanagementsystem/fxml/login.fxml"
                )
            );

            //this creates the controller and injects all fields
            Scene scene = new Scene(loader.load(), 1100, 700);

            // Now tell the controller which role is logging in
            LoginController loginController = loader.getController();
            loginController.setRole(role);

            // Switch the scene
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
