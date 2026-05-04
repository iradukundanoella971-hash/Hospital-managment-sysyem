package org.example.hospitalmanagmentsystem.controller;

import org.example.hospitalmanagmentsystem.backend.*;
import org.example.hospitalmanagmentsystem.component.AlertUtil;

// JavaFX imports - THESE ARE CRITICAL
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DoctorPortalController implements Initializable {

    // FXML Fields
    @FXML private TextField loginNameField;
    @FXML private TextField diagnosisField;
    @FXML private TextField medicineField;
    @FXML private PasswordField loginPasswordField;
    @FXML private ComboBox<String> patientRecordCombo;
    @FXML private ComboBox<String> viewRecordPatientCombo;
    @FXML private Label loginStatusLabel;
    @FXML private Label recordStatusLabel;
    @FXML private Label welcomeLabel;
    @FXML private TitledPane loginPane;
    @FXML private VBox doctorDashboard;
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> colPatientName;
    @FXML private TableColumn<Appointment, String> colTime;
    @FXML private ListView<String> recordsListView;

    private Doctor loggedInDoctor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
    }

    private void setupTableColumns() {
        colPatientName.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPatient().getName()));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    @FXML
    private void handleLogin() {
        try {
            String name = loginNameField.getText();
            String password = loginPasswordField.getText();

            if (name == null || name.trim().isEmpty()) {
                AlertUtil.showError(loginStatusLabel, "Please enter doctor name");
                return;
            }

            Doctor doctor = MainViewController.getHospital().getDoctors().get(name);

            if (doctor != null && doctor.login(name, password)) {
                loggedInDoctor = doctor;
                loginPane.setExpanded(false);
                doctorDashboard.setVisible(true);
                doctorDashboard.setManaged(true);
                welcomeLabel.setText("Welcome, Dr. " + doctor.getName() + "!");
                refreshAppointments();
                loadPatientsForRecords();
                AlertUtil.showSuccess(loginStatusLabel, "Login successful!");
            } else {
                AlertUtil.showError(loginStatusLabel, "Invalid credentials!");
            }
        } catch (Exception e) {
            AlertUtil.showError(loginStatusLabel, "Login failed: " + e.getMessage());
        }
    }

    @FXML
    private void refreshAppointments() {
        Platform.runLater(() -> {
            if (loggedInDoctor != null && appointmentsTable != null) {
                ObservableList<Appointment> appointments = FXCollections.observableArrayList();
                appointments.addAll(loggedInDoctor.getAppointments());
                appointmentsTable.setItems(appointments);
            }
        });
    }

    private void loadPatientsForRecords() {
        Platform.runLater(() -> {
            if (patientRecordCombo != null && viewRecordPatientCombo != null) {
                patientRecordCombo.getItems().clear();
                viewRecordPatientCombo.getItems().clear();
                patientRecordCombo.getItems().addAll(MainViewController.getHospital().getPatients().keySet());
                viewRecordPatientCombo.getItems().addAll(MainViewController.getHospital().getPatients().keySet());
            }
        });
    }

    @FXML
    private void handleAddMedicalRecord() {
        try {
            String patientName = patientRecordCombo.getValue();
            String diagnosis = diagnosisField.getText();
            String medicine = medicineField.getText();

            if (patientName == null || patientName.trim().isEmpty()) {
                AlertUtil.showError(recordStatusLabel, "Please select a patient");
                return;
            }

            if (diagnosis == null || diagnosis.trim().isEmpty()) {
                AlertUtil.showError(recordStatusLabel, "Please enter diagnosis");
                return;
            }

            if (medicine == null || medicine.trim().isEmpty()) {
                AlertUtil.showError(recordStatusLabel, "Please enter medicine");
                return;
            }

            Patient patient = MainViewController.getHospital().getPatients().get(patientName);
            if (patient == null) {
                AlertUtil.showError(recordStatusLabel, "Patient not found");
                return;
            }

            MedicalRecord record = new MedicalRecord(diagnosis, medicine);
            patient.addRecord(record);

            AlertUtil.showSuccess(recordStatusLabel, "Medical record added for " + patientName);
            diagnosisField.clear();
            medicineField.clear();

        } catch (Exception e) {
            AlertUtil.showError(recordStatusLabel, "Failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewRecords() {
        try {
            String patientName = viewRecordPatientCombo.getValue();
            if (patientName == null || patientName.trim().isEmpty()) {
                AlertUtil.showError(recordStatusLabel, "Please select a patient");
                return;
            }

            Patient patient = MainViewController.getHospital().getPatients().get(patientName);
            if (patient == null) {
                AlertUtil.showError(recordStatusLabel, "Patient not found");
                return;
            }

            Platform.runLater(() -> {
                recordsListView.getItems().clear();
                if (patient.getRecords().isEmpty()) {
                    recordsListView.getItems().add("No medical records available");
                } else {
                    for (MedicalRecord record : patient.getRecords()) {
                        recordsListView.getItems().add(
                                "🏥 Diagnosis: " + record.getDescription() +
                                        "\n💊 Medicine: " + record.getMedicine() + "\n"
                        );
                    }
                }
            });

        } catch (Exception e) {
            AlertUtil.showError(recordStatusLabel, "Failed to load records: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        loggedInDoctor = null;
        loginPane.setExpanded(true);
        doctorDashboard.setVisible(false);
        doctorDashboard.setManaged(false);
        loginNameField.clear();
        loginPasswordField.clear();
        loginStatusLabel.setText("");
        AlertUtil.showSuccess(loginStatusLabel, "Logged out successfully");
    }
}