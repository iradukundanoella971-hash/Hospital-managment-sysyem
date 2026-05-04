package org.example.hospitalmanagmentsystem.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hospitalmanagmentsystem.backend.Doctor;
import org.example.hospitalmanagmentsystem.backend.Patient;
import org.example.hospitalmanagmentsystem.component.HospitalContext;
import org.example.hospitalmanagmentsystem.component.SessionManager;
import org.example.hospitalmanagmentsystem.ui.SceneNavigator;

public class AdminDashboardController {
    @FXML private TableView<Doctor> doctorsTable;
    @FXML private TableColumn<Doctor, String> doctorIdCol;
    @FXML private TableColumn<Doctor, String> doctorNameCol;
    @FXML private TableColumn<Doctor, String> doctorSpecCol;
    @FXML private TableView<Patient> patientsTable;
    @FXML private TableColumn<Patient, String> patientIdCol;
    @FXML private TableColumn<Patient, String> patientNameCol;
    @FXML private Label summaryLabel;
    @FXML private TextField doctorNameField;
    @FXML private TextField doctorAgeField;
    @FXML private TextField doctorSpecField;
    @FXML private TextField doctorUserField;
    @FXML private PasswordField doctorPassField;

    @FXML
    public void initialize() {
        doctorIdCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDoctorId()));
        doctorNameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        doctorSpecCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSpecialization()));
        patientIdCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
        patientNameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        refresh();
    }

    @FXML
    private void handleAddDoctor() {
        try {
            String id = HospitalContext.getInstance().getIdGenerator().nextDoctorId();
            Doctor doctor = new Doctor(id, doctorNameField.getText(), Integer.parseInt(doctorAgeField.getText()), doctorSpecField.getText(), doctorPassField.getText());
            HospitalContext.getInstance().getHospital().registerDoctor(doctor);
            HospitalContext.getInstance().getAuthService().createDoctorAccount(doctor, doctorUserField.getText(), doctorPassField.getText());
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            refresh();
        } catch (Exception e) {
            summaryLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRemoveDoctor() {
        Doctor doctor = doctorsTable.getSelectionModel().getSelectedItem();
        if (doctor == null) return;
        HospitalContext.getInstance().getHospital().getDoctors().remove(doctor.getDoctorId());
        refresh();
    }

    @FXML
    private void handleRefresh() {
        refresh();
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneNavigator.navigate("/fxml/Login.fxml", "Hospital Management System");
    }

    private void refresh() {
        Platform.runLater(() -> {
            ObservableList<Doctor> doctors = FXCollections.observableArrayList(HospitalContext.getInstance().getHospital().getDoctors().values());
            ObservableList<Patient> patients = FXCollections.observableArrayList(HospitalContext.getInstance().getHospital().getPatients().values());
            doctors.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            patients.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            doctorsTable.setItems(doctors);
            patientsTable.setItems(patients);
            summaryLabel.setText("Doctors: " + doctors.size() + " | Patients: " + patients.size() + " | Appointments: "
                    + HospitalContext.getInstance().getHospital().getAppointments().size());
        });
    }
}
