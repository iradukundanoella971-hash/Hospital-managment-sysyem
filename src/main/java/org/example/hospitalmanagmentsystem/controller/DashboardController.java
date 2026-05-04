package org.example.hospitalmanagmentsystem.controller;

import org.example.hospitalmanagmentsystem.backend.Hospital;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label doctorCount;
    @FXML private Label patientCount;
    @FXML private Label appointmentCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateStats();
    }

    private void updateStats() {
        Platform.runLater(() -> {
            Hospital hospital = MainViewController.getHospital();
            doctorCount.setText(String.valueOf(hospital.getDoctors().size()));
            patientCount.setText(String.valueOf(hospital.getPatients().size()));
            int totalAppointments = hospital.getDoctors().values().stream()
                    .mapToInt(d -> d.getAppointments().size())
                    .sum();
            appointmentCount.setText(String.valueOf(totalAppointments));
        });
    }

    @FXML
    private void quickAddDoctor() {
        MainViewController.refreshData();
    }

    @FXML
    private void quickAddPatient() {
        MainViewController.refreshData();
    }

    @FXML
    private void quickBookAppointment() {
        MainViewController.refreshData();
    }
}
