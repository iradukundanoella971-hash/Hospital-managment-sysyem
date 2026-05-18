package org.example.hospitalmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.hospitalmanagementsystem.backend.Appointment;
import org.example.hospitalmanagementsystem.backend.Doctor;
import org.example.hospitalmanagementsystem.backend.Patient;
import org.example.hospitalmanagementsystem.database.HospitalDatabase;

import java.io.IOException;
import java.sql.SQLException;

public class AdminController {

    // ── Panels ───────────────────────────────────────────────────────────────
    @FXML private VBox dashboardPanel;
    @FXML private VBox doctorPanel;
    @FXML private VBox patientPanel;
    @FXML private VBox appointmentPanel;

    // ── Dashboard ────────────────────────────────────────────────────────────
    @FXML private Label welcomeLabel;
    @FXML private Label totalDoctorsLabel;
    @FXML private Label totalPatientsLabel;
    @FXML private Label totalAppointmentsLabel;

    // ── Doctor form ──────────────────────────────────────────────────────────
    @FXML private TextField     doctorIdField;
    @FXML private TextField     doctorNameField;
    @FXML private TextField     doctorPhoneField;
    @FXML private TextField     doctorSpecField;
    @FXML private TextField     doctorLocationField;
    @FXML private TextField     doctorAgeField;
    @FXML private TextField     doctorUsernameField;
    @FXML private PasswordField doctorPasswordField;
    @FXML private TextField     doctorSearchField;
    @FXML private Label         doctorMessageLabel;

    // ── Doctor TableView ─────────────────────────────────────────────────────
    @FXML private TableView<Doctor>            doctorTable;
    @FXML private TableColumn<Doctor, String>  colDoctorId;
    @FXML private TableColumn<Doctor, String>  colDoctorName;
    @FXML private TableColumn<Doctor, String>  colDoctorPhone;
    @FXML private TableColumn<Doctor, String>  colDoctorSpec;
    @FXML private TableColumn<Doctor, String>  colDoctorLocation;
    @FXML private TableColumn<Doctor, Integer> colDoctorAge;
    @FXML private TableColumn<Doctor, String>  colDoctorUsername;

    // ── Patient TableView ────────────────────────────────────────────────────
    @FXML private TableView<Patient>           patientTable;
    @FXML private TableColumn<Patient, String> colPatientId;
    @FXML private TableColumn<Patient, String> colPatientName;
    @FXML private TableColumn<Patient, String> colPatientLocation;
    @FXML private TableColumn<Patient, String> colPatientUsername;

    // ── Appointment TableView ────────────────────────────────────────────────
    @FXML private TableView<Appointment>            appointmentTable;
    @FXML private TableColumn<Appointment, Integer> colApptId;
    @FXML private TableColumn<Appointment, String>  colApptDoctor;
    @FXML private TableColumn<Appointment, String>  colApptPatient;
    @FXML private TableColumn<Appointment, String>  colApptDate;
    @FXML private TableColumn<Appointment, String>  colApptTime;
    @FXML private TableColumn<Appointment, String>  colApptStatus;

    private String loggedInUsername;

    public void setUsername(String username) {
        this.loggedInUsername = username;
        welcomeLabel.setText("Welcome, " + username + "!");
        showDashboard();
    }

    // ── Sidebar ──────────────────────────────────────────────────────────────

    @FXML private void showDashboard() {
        showPanel(dashboardPanel);
        totalDoctorsLabel.setText(String.valueOf(HospitalDatabase.getCount("doctor")));
        totalPatientsLabel.setText(String.valueOf(HospitalDatabase.getCount("patient")));
        totalAppointmentsLabel.setText(String.valueOf(HospitalDatabase.getCount("appointment")));
    }

    @FXML private void showDoctors() {
        showPanel(doctorPanel);
        setupDoctorTable();
        loadDoctors("");
    }

    @FXML private void showPatients() {
        showPanel(patientPanel);
        setupPatientTable();
        loadPatients();
    }

    @FXML private void showAppointments() {
        showPanel(appointmentPanel);
        setupAppointmentTable();
        loadAppointments();
    }

    private void showPanel(VBox panel) {
        dashboardPanel.setVisible(false);   dashboardPanel.setManaged(false);
        doctorPanel.setVisible(false);      doctorPanel.setManaged(false);
        patientPanel.setVisible(false);     patientPanel.setManaged(false);
        appointmentPanel.setVisible(false); appointmentPanel.setManaged(false);
        panel.setVisible(true);
        panel.setManaged(true);
    }

    // ── Doctor CRUD ──────────────────────────────────────────────────────────

    private void setupDoctorTable() {
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDoctorPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colDoctorSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colDoctorLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDoctorAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colDoctorUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void loadDoctors(String searchText) {
        try {
            doctorTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getAllDoctors(searchText))
            );
        } catch (SQLException e) {
            doctorMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddDoctor() {
        String doctorId = doctorIdField.getText().trim();
        String name     = doctorNameField.getText().trim();
        String phone    = doctorPhoneField.getText().trim();
        String spec     = doctorSpecField.getText().trim();
        String location = doctorLocationField.getText().trim();
        String ageText  = doctorAgeField.getText().trim();
        String username = doctorUsernameField.getText().trim();
        String password = doctorPasswordField.getText().trim();

        if (doctorId.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty() || ageText.isEmpty()) {
            doctorMessageLabel.setText("Please fill in all required fields.");
            return;
        }

        int age;
        try { age = Integer.parseInt(ageText); }
        catch (NumberFormatException e) { doctorMessageLabel.setText("Age must be a number."); return; }

        if (age <= 18 || age > 60) { doctorMessageLabel.setText("Age must be between 19 and 60."); return; }

        try {
            HospitalDatabase.insertDoctor(new Doctor(doctorId, name, phone, spec, location, age, username, password));
            doctorMessageLabel.setStyle("-fx-text-fill: #27ae60;");
            doctorMessageLabel.setText("Doctor added successfully!");
            clearDoctorForm();
            loadDoctors("");
        } catch (SQLException e) {
            doctorMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique"))
                doctorMessageLabel.setText("Doctor ID or Username already exists.");
            else
                doctorMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSelectDoctor() {
        Doctor d = doctorTable.getSelectionModel().getSelectedItem();
        if (d == null) return;
        doctorIdField.setText(d.getDoctorId());
        doctorNameField.setText(d.getName());
        doctorPhoneField.setText(d.getPhoneNumber());
        doctorSpecField.setText(d.getSpecialization());
        doctorLocationField.setText(d.getLocation());
        doctorAgeField.setText(String.valueOf(d.getAge()));
        doctorUsernameField.setText(d.getUsername());
        doctorPasswordField.setText(d.getPassword());
    }

    @FXML
    private void handleUpdateDoctor() {
        String doctorId = doctorIdField.getText().trim();
        if (doctorId.isEmpty()) { doctorMessageLabel.setText("Select a doctor to update."); return; }

        int age;
        try { age = Integer.parseInt(doctorAgeField.getText().trim()); }
        catch (NumberFormatException e) { doctorMessageLabel.setText("Age must be a number."); return; }

        if (age <= 18 || age > 60) { doctorMessageLabel.setText("Age must be between 19 and 60."); return; }

        try {
            HospitalDatabase.updateDoctor(new Doctor(
                doctorId,
                doctorNameField.getText().trim(),
                doctorPhoneField.getText().trim(),
                doctorSpecField.getText().trim(),
                doctorLocationField.getText().trim(),
                age,
                doctorUsernameField.getText().trim(),
                doctorPasswordField.getText().trim()
            ));
            doctorMessageLabel.setStyle("-fx-text-fill: #27ae60;");
            doctorMessageLabel.setText("Doctor updated successfully!");
            clearDoctorForm();
            loadDoctors("");
        } catch (SQLException e) {
            doctorMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteDoctor() {
        Doctor d = doctorTable.getSelectionModel().getSelectedItem();
        if (d == null) { doctorMessageLabel.setText("Please select a doctor to delete."); return; }

        try {
            HospitalDatabase.deleteDoctor(d.getDoctorId());
            doctorMessageLabel.setStyle("-fx-text-fill: #27ae60;");
            doctorMessageLabel.setText("Doctor deleted.");
            clearDoctorForm();
            loadDoctors("");
        } catch (SQLException e) {
            doctorMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchDoctor() {
        loadDoctors(doctorSearchField.getText().trim());
    }

    private void clearDoctorForm() {
        doctorIdField.clear(); doctorNameField.clear(); doctorPhoneField.clear();
        doctorSpecField.clear(); doctorLocationField.clear(); doctorAgeField.clear();
        doctorUsernameField.clear(); doctorPasswordField.clear();
    }

    // ── Patient View ─────────────────────────────────────────────────────────

    private void setupPatientTable() {
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPatientLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colPatientUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void loadPatients() {
        try {
            patientTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getAllPatients())
            );
        } catch (SQLException e) {
            System.out.println("Error loading patients: " + e.getMessage());
        }
    }

    // ── Appointment View ─────────────────────────────────────────────────────

    private void setupAppointmentTable() {
        colApptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colApptDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colApptPatient.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colApptDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colApptTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colApptStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAppointments() {
        try {
            appointmentTable.setItems(
                FXCollections.observableArrayList(HospitalDatabase.getAllAppointments())
            );
        } catch (SQLException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    // ── Logout ───────────────────────────────────────────────────────────────

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
