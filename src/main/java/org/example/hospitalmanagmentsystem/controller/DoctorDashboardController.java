package org.example.hospitalmanagmentsystem.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hospitalmanagmentsystem.backend.Appointment;
import org.example.hospitalmanagmentsystem.backend.Doctor;
import org.example.hospitalmanagmentsystem.backend.MedicalRecord;
import org.example.hospitalmanagmentsystem.backend.Patient;
import org.example.hospitalmanagmentsystem.backend.UserAccount;
import org.example.hospitalmanagmentsystem.component.HospitalContext;
import org.example.hospitalmanagmentsystem.component.SessionManager;
import org.example.hospitalmanagmentsystem.ui.SceneNavigator;

import java.time.LocalDate;

public class DoctorDashboardController {
    @FXML private Label doctorLabel;
    @FXML private TableView<Appointment> todayTable;
    @FXML private TableColumn<Appointment, String> patientCol;
    @FXML private TableColumn<Appointment, String> dateCol;
    @FXML private TableColumn<Appointment, String> timeCol;
    @FXML private TextField patientIdField;
    @FXML private TextArea diagnosisArea;
    @FXML private TextField medicineField;
    @FXML private DatePicker followUpDate;
    @FXML private Label feedbackLabel;

    private Doctor doctor;

    @FXML
    public void initialize() {
        UserAccount user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            SceneNavigator.navigate("/fxml/Login.fxml", "Hospital Management System");
            return;
        }
        doctor = HospitalContext.getInstance().getHospital().getDoctors().get(user.getLinkedId());
        doctorLabel.setText("Welcome Dr. " + doctor.getName());
        patientCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPatientName()));
        dateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getDate())));
        timeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getTimeValue())));
        refreshToday();
    }

    @FXML
    private void handleAddRecord() {
        try {
            Patient p = HospitalContext.getInstance().getHospital().getPatients().get(patientIdField.getText());
            if (p == null) throw new IllegalArgumentException("Patient ID not found");
            p.addRecord(new MedicalRecord(diagnosisArea.getText(), medicineField.getText()));
            feedbackLabel.setText("Record and prescription saved.");
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
        } catch (Exception e) {
            feedbackLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleScheduleFollowUp() {
        try {
            Patient p = HospitalContext.getInstance().getHospital().getPatients().get(patientIdField.getText());
            if (p == null) throw new IllegalArgumentException("Patient ID not found");
            if (followUpDate.getValue() == null || !followUpDate.getValue().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Follow-up must be a future date");
            }
            feedbackLabel.setText("Follow-up planned for " + followUpDate.getValue());
        } catch (Exception e) {
            feedbackLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        refreshToday();
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneNavigator.navigate("/fxml/Login.fxml", "Hospital Management System");
    }

    private void refreshToday() {
        Platform.runLater(() -> todayTable.setItems(FXCollections.observableArrayList(
                HospitalContext.getInstance().getHospital().getAppointmentsForDoctor(doctor.getDoctorId()).stream()
                        .filter(a -> LocalDate.now().equals(a.getDate()))
                        .toList()
        )));
    }
}
