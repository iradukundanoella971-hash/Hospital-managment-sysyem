package org.example.hospitalmanagmentsystem.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.example.hospitalmanagmentsystem.backend.Hospital;
import org.example.hospitalmanagmentsystem.component.AuthService;
import org.example.hospitalmanagmentsystem.component.DataLoader;
import org.example.hospitalmanagmentsystem.component.HospitalContext;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainViewController implements Initializable {

    @FXML private StackPane contentArea;

    private static Hospital hospital;
    private static AuthService authService;
    private static MainViewController instance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        hospital = HospitalContext.getInstance().getHospital();
        authService = HospitalContext.getInstance().getAuthService();
        DataLoader.loadSampleData(hospital, authService);
        showDashboard();
    }

    public static Hospital getHospital() {
        return hospital;
    }

    public static AuthService getAuthService() {
        return authService;
    }

    public static void refreshData() {
        // Refresh any open views if needed
    }

    public void showDashboard() {
        loadView("/Dashboard.fxml");
    }

    public void showDoctorManagement() {
        loadView("/DoctorManagement.fxml");
    }

    public void showPatientManagement() {
        loadView("/PatientManagement.fxml");
    }

    public void showAppointmentBooking() {
        loadView("/AppointmentBooking.fxml");
    }

    public void showDoctorPortal() {
        loadView("/fxml/DoctorPortal.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}