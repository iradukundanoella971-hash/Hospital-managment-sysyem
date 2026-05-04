package org.example.hospitalmanagmentsystem.controller;


import org.example.hospitalmanagmentsystem.backend.*;
import org.example.hospitalmanagmentsystem.component.AlertUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML private ComboBox<String> patientCombo;
    @FXML private ComboBox<String> specializationCombo;
    @FXML private ComboBox<String> timeCombo;
    @FXML private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPatients();
        setupSpecializations();
        setupTimes();
    }

    private void loadPatients() {
        Platform.runLater(() -> {
            patientCombo.getItems().clear();
            patientCombo.getItems().addAll(
                    MainViewController.getHospital().getPatients().keySet()
            );
        });
    }

    private void setupSpecializations() {
        specializationCombo.setItems(FXCollections.observableArrayList(
                "Cardiology", "Neurology", "Pediatrics", "Orthopedics",
                "Dermatology", "Radiology", "Emergency Medicine", "Oncology"
        ));
    }

    private void setupTimes() {
        timeCombo.setItems(FXCollections.observableArrayList(
                "09:00 AM", "10:00 AM", "11:00 AM", "02:00 PM", "03:00 PM", "04:00 PM"
        ));
    }

    @FXML
    private void handleBookAppointment() {
        try {
            String patientName = patientCombo.getValue();
            String specialization = specializationCombo.getValue();
            String time = timeCombo.getValue();

            if (patientName == null || specialization == null || time == null) {
                AlertUtil.showError(statusLabel, "Please fill all fields");
                return;
            }

            Patient patient = MainViewController.getHospital().getPatients().get(patientName);
            if (patient == null) {
                AlertUtil.showError(statusLabel, "Patient not found");
                return;
            }

            MainViewController.getHospital().bookAppointment(specialization, patient, time);
            AlertUtil.showSuccess(statusLabel, "Appointment booked successfully for " + patientName + " at " + time);

            // Clear selections
            patientCombo.setValue(null);
            specializationCombo.setValue(null);
            timeCombo.setValue(null);

        } catch (InsufficientDoctorException e) {
            AlertUtil.showError(statusLabel, e.getMessage());
        } catch (RuntimeException e) {
            AlertUtil.showError(statusLabel, e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError(statusLabel, "Booking failed: " + e.getMessage());
        }
    }
}