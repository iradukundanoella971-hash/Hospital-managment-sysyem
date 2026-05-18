package org.example.hospitalmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.hospitalmanagementsystem.backend.Appointment;
import org.example.hospitalmanagementsystem.backend.Doctor;
import org.example.hospitalmanagementsystem.backend.MedicalRecord;
import org.example.hospitalmanagementsystem.database.HospitalDatabase;

import java.io.IOException;
import java.sql.SQLException;

public class PatientController {

    // ── Panels ───────────────────────────────────────────────────────────────
    @FXML private VBox dashboardPanel;
    @FXML private VBox doctorPanel;
    @FXML private VBox appointmentPanel;
    @FXML private VBox historyPanel;

    // ── Dashboard ────────────────────────────────────────────────────────────
    @FXML private Label welcomeLabel;
    @FXML private Label myAppointmentsLabel;
    @FXML private Label myRecordsLabel;

    // ── Doctor TableView ─────────────────────────────────────────────────────
    @FXML private TableView<Doctor>           doctorTable;
    @FXML private TableColumn<Doctor, String> colDoctorName;
    @FXML private TableColumn<Doctor, String> colDoctorSpec;
    @FXML private TableColumn<Doctor, String> colDoctorLocation;

    // ── Appointment booking form ─────────────────────────────────────────────
    @FXML private ComboBox<String> doctorCombo;
    @FXML private TextField        apptDateField;
    @FXML private TextField        apptTimeField;
    @FXML private Label            apptMessageLabel;

    // ── Appointment TableView ────────────────────────────────────────────────
    @FXML private TableView<Appointment>            appointmentTable;
    @FXML private TableColumn<Appointment, Integer> colApptId;
    @FXML private TableColumn<Appointment, String>  colApptDoctor;
    @FXML private TableColumn<Appointment, String>  colApptDate;
    @FXML private TableColumn<Appointment, String>  colApptTime;
    @FXML private TableColumn<Appointment, String>  colApptStatus;

    // ── Medical History TableView ────────────────────────────────────────────
    @FXML private TableView<MedicalRecord>            historyTable;
    @FXML private TableColumn<MedicalRecord, Integer> colHistId;
    @FXML private TableColumn<MedicalRecord, String>  colHistDoctor;
    @FXML private TableColumn<MedicalRecord, String>  colHistDiagnosis;
    @FXML private TableColumn<MedicalRecord, String>  colHistMedicine;
    @FXML private TableColumn<MedicalRecord, String>  colHistFollowUp;
    @FXML private TableColumn<MedicalRecord, String>  colHistNextAppt;

    private String loggedInUsername;
    private String patientId;

    public void setUsername(String username) {
        this.loggedInUsername = username;
        welcomeLabel.setText("Welcome, " + username + "!");
        try {
            patientId = HospitalDatabase.getPatientIdByUsername(username);
        } catch (SQLException e) {
            System.out.println("Error loading patient ID: " + e.getMessage());
        }
        showDashboard();
    }

    // ── Sidebar ──────────────────────────────────────────────────────────────

    @FXML private void showDashboard() {
        showPanel(dashboardPanel);
        myAppointmentsLabel.setText(String.valueOf(HospitalDatabase.getAppointmentCountForPatient(patientId)));
        myRecordsLabel.setText(String.valueOf(HospitalDatabase.getRecordCountForPatient(patientId)));
    }

    @FXML private void showDoctors() {
        showPanel(doctorPanel);
        setupDoctorTable();
        loadAllDoctors();
    }

    @FXML private void showAppointments() {
        showPanel(appointmentPanel);
        setupAppointmentTable();
        loadMyAppointments();
        loadDoctorCombo();
    }

    @FXML private void showHistory() {
        showPanel(historyPanel);
        setupHistoryTable();
        loadMyHistory();
    }

    private void showPanel(VBox panel) {
        dashboardPanel.setVisible(false);   dashboardPanel.setManaged(false);
        doctorPanel.setVisible(false);      doctorPanel.setManaged(false);
        appointmentPanel.setVisible(false); appointmentPanel.setManaged(false);
        historyPanel.setVisible(false);     historyPanel.setManaged(false);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    // ── Doctors ──────────────────────────────────────────────────────────────

    private void setupDoctorTable() {
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDoctorSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colDoctorLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
    }

    private void loadAllDoctors() {
        try {
            doctorTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getAllDoctors(""))
            );
        } catch (SQLException e) {
            System.out.println("Error loading doctors: " + e.getMessage());
        }
    }

    // ── Appointments ─────────────────────────────────────────────────────────

    private void loadDoctorCombo() {
        try {
            doctorCombo.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getAllDoctorIds())
            );
        } catch (SQLException e) {
            System.out.println("Error loading doctor combo: " + e.getMessage());
        }
    }

    private void setupAppointmentTable() {
        colApptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colApptDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colApptDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colApptTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colApptStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadMyAppointments() {
        try {
            appointmentTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getAppointmentsByPatient(patientId))
            );
        } catch (SQLException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    @FXML
    private void handleBookAppointment() {
        String selectedDoctorId = doctorCombo.getValue();
        String date = apptDateField.getText().trim();
        String time = apptTimeField.getText().trim();

        if (selectedDoctorId == null || date.isEmpty() || time.isEmpty()) {
            apptMessageLabel.setText("Please fill in all fields.");
            return;
        }

        // Validate date and time format before sending to DB
        try {
            java.sql.Date.valueOf(date);
            java.sql.Time.valueOf(time + ":00");
        } catch (IllegalArgumentException e) {
            apptMessageLabel.setText("Use date format YYYY-MM-DD and time HH:MM");
            return;
        }

        try {
            boolean booked = HospitalDatabase.insertAppointment(selectedDoctorId, patientId, date, time);
            if (booked) {
                apptMessageLabel.setStyle("-fx-text-fill: #27ae60;");
                apptMessageLabel.setText("Appointment booked successfully!");
                loadMyAppointments();
            } else {
                apptMessageLabel.setText("You already have this appointment booked.");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate"))
                apptMessageLabel.setText("That time slot is already taken for this doctor.");
            else
                apptMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    // ── Medical History ──────────────────────────────────────────────────────

    private void setupHistoryTable() {
        colHistId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHistDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colHistDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        colHistMedicine.setCellValueFactory(new PropertyValueFactory<>("medicine"));
        colHistFollowUp.setCellValueFactory(new PropertyValueFactory<>("followUpNote"));
        colHistNextAppt.setCellValueFactory(new PropertyValueFactory<>("nextAppointment"));
    }

    private void loadMyHistory() {
        try {
            historyTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getRecordsByPatient(patientId))
            );
        } catch (SQLException e) {
            System.out.println("Error loading history: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/org/example/hospitalmanagementsystem/fxml/home.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 1100, 700));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
