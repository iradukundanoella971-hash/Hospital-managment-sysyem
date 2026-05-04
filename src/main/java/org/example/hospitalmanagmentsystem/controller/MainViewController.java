package org.example.hospitalmanagmentsystem.controller;

import org.example.hospitalmanagmentsystem.backend.Hospital;
import org.example.hospitalmanagmentsystem.component.DataLoader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML private StackPane contentArea;

    private static Hospital hospital;
    private static MainViewController instance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        hospital = new Hospital();
        DataLoader.loadSampleData(hospital);
        showDashboard();
    }

    public static Hospital getHospital() {
        return hospital;
    }

    public static void refreshData() {
        // Refresh any open views if needed
    }

    public void showDashboard() {
        loadView("/fxml/Dashboard.fxml");
    }

    public void showDoctorManagement() {
        loadView("/fxml/DoctorManagement.fxml");
    }

    public void showPatientManagement() {
        loadView("/fxml/PatientManagement.fxml");
    }

    public void showAppointmentBooking() {
        loadView("/fxml/AppointmentBooking.fxml");
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