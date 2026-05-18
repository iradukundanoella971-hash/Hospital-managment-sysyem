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
import org.example.hospitalmanagementsystem.backend.MedicalRecord;
import org.example.hospitalmanagementsystem.backend.Patient;
import org.example.hospitalmanagementsystem.database.HospitalDatabase;

import java.io.IOException;
import java.sql.SQLException;

public class DoctorController {

    // ── Panels ───────────────────────────────────────────────────────────────
    @FXML private VBox dashboardPanel;
    @FXML private VBox patientPanel;
    @FXML private VBox appointmentPanel;
    @FXML private VBox medicalRecordPanel;

    // ── Dashboard ────────────────────────────────────────────────────────────
    @FXML private Label welcomeLabel;
    @FXML private Label totalPatientsLabel;
    @FXML private Label totalAppointmentsLabel;

    // ── Patient TableView ────────────────────────────────────────────────────
    @FXML private TableView<Patient>           patientTable;
    @FXML private TableColumn<Patient, String> colPatientId;
    @FXML private TableColumn<Patient, String> colPatientName;
    @FXML private TableColumn<Patient, String> colPatientLocation;

    // ── Appointment TableView ────────────────────────────────────────────────
    @FXML private TableView<Appointment>            appointmentTable;
    @FXML private TableColumn<Appointment, Integer> colApptId;
    @FXML private TableColumn<Appointment, String>  colApptPatient;
    @FXML private TableColumn<Appointment, String>  colApptDate;
    @FXML private TableColumn<Appointment, String>  colApptTime;
    @FXML private TableColumn<Appointment, String>  colApptStatus;

    // ── Medical Record form ──────────────────────────────────────────────────
    @FXML private ComboBox<String> appointmentCombo;
    @FXML private TextArea         diagnosisArea;
    @FXML private TextArea         medicineArea;
    @FXML private TextArea         descriptionArea;
    @FXML private TextArea         treatmentArea;
    @FXML private TextArea         followUpArea;
    @FXML private TextField        nextAppointmentField;
    @FXML private Label            recordMessageLabel;

    // ── Medical Record TableView ─────────────────────────────────────────────
    @FXML private TableView<MedicalRecord>            recordTable;
    @FXML private TableColumn<MedicalRecord, Integer> colRecordId;
    @FXML private TableColumn<MedicalRecord, Integer> colRecordApptId;
    @FXML private TableColumn<MedicalRecord, String>  colRecordPatient;
    @FXML private TableColumn<MedicalRecord, String>  colRecordDiagnosis;
    @FXML private TableColumn<MedicalRecord, String>  colRecordMedicine;

    private String loggedInUsername;
    private String doctorId;

    public void setUsername(String username) {
        this.loggedInUsername = username;
        welcomeLabel.setText("Welcome, Dr. " + username + "!");
        try {
            doctorId = HospitalDatabase.getDoctorIdByUsername(username);
        } catch (SQLException e) {
            System.out.println("Error loading doctor ID: " + e.getMessage());
        }
        showDashboard();
    }

    // ── Sidebar ──────────────────────────────────────────────────────────────

    @FXML private void showDashboard() {
        showPanel(dashboardPanel);
        totalPatientsLabel.setText(String.valueOf(HospitalDatabase.getPatientCountForDoctor(doctorId)));
        totalAppointmentsLabel.setText(String.valueOf(HospitalDatabase.getAppointmentCountForDoctor(doctorId)));
    }

    @FXML private void showPatients() {
        showPanel(patientPanel);
        setupPatientTable();
        loadMyPatients();
    }

    @FXML private void showAppointments() {
        showPanel(appointmentPanel);
        setupAppointmentTable();
        loadMyAppointments();
    }

    @FXML private void showMedicalRecords() {
        showPanel(medicalRecordPanel);
        setupRecordTable();
        loadMyRecords();
        loadAppointmentCombo();
    }

    private void showPanel(VBox panel) {
        dashboardPanel.setVisible(false);     dashboardPanel.setManaged(false);
        patientPanel.setVisible(false);       patientPanel.setManaged(false);
        appointmentPanel.setVisible(false);   appointmentPanel.setManaged(false);
        medicalRecordPanel.setVisible(false); medicalRecordPanel.setManaged(false);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    // ── Patients ─────────────────────────────────────────────────────────────

    private void setupPatientTable() {
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPatientLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
    }

    private void loadMyPatients() {
        try {
            patientTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getPatientsByDoctor(doctorId))
            );
        } catch (SQLException e) {
            System.out.println("Error loading patients: " + e.getMessage());
        }
    }

    // ── Appointments ─────────────────────────────────────────────────────────

    private void setupAppointmentTable() {
        colApptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colApptPatient.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colApptDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colApptTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colApptStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadMyAppointments() {
        try {
            appointmentTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getAppointmentsByDoctor(doctorId))
            );
        } catch (SQLException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    // ── Medical Records ──────────────────────────────────────────────────────

    private void setupRecordTable() {
        colRecordId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRecordApptId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colRecordPatient.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colRecordDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        colRecordMedicine.setCellValueFactory(new PropertyValueFactory<>("medicine"));
    }

    private void loadMyRecords() {
        try {
            recordTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getRecordsByDoctor(doctorId))
            );
        } catch (SQLException e) {
            System.out.println("Error loading records: " + e.getMessage());
        }
    }

    private void loadAppointmentCombo() {
        try {
            appointmentCombo.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getAppointmentIdsByDoctor(doctorId))
            );
        } catch (SQLException e) {
            System.out.println("Error loading combo: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveRecord() {
        String apptIdStr = appointmentCombo.getValue();
        if (apptIdStr == null) { recordMessageLabel.setText("Please select an appointment."); return; }

        int apptId = Integer.parseInt(apptIdStr);

        try {
            String patientId = HospitalDatabase.getPatientIdByAppointment(apptId);
            if (patientId == null) { recordMessageLabel.setText("Appointment not found."); return; }

            HospitalDatabase.saveRecord(
                apptId, doctorId, patientId,
                diagnosisArea.getText(),
                medicineArea.getText(),
                descriptionArea.getText(),
                treatmentArea.getText(),
                followUpArea.getText(),
                nextAppointmentField.getText().trim()
            );
            recordMessageLabel.setStyle("-fx-text-fill: #27ae60;");
            recordMessageLabel.setText("Medical record saved!");
            loadMyRecords();

        } catch (SQLException e) {
            recordMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSelectRecord() {
        MedicalRecord r = recordTable.getSelectionModel().getSelectedItem();
        if (r == null) return;
        appointmentCombo.setValue(String.valueOf(r.getAppointmentId()));
        diagnosisArea.setText(r.getDiagnosis());
        medicineArea.setText(r.getMedicine());
        descriptionArea.setText(r.getDescription());
        treatmentArea.setText(r.getTreatmentNotes());
        followUpArea.setText(r.getFollowUpNote());
        nextAppointmentField.setText(r.getNextAppointment());
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
