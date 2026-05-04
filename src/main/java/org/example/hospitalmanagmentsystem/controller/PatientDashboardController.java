package org.example.hospitalmanagmentsystem.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hospitalmanagmentsystem.backend.*;
import org.example.hospitalmanagmentsystem.component.*;
import org.example.hospitalmanagmentsystem.ui.SceneNavigator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

public class PatientDashboardController {
    @FXML private Label patientLabel;
    @FXML private ComboBox<String> doctorCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private Label bookingFeedback;
    @FXML private ListView<String> recordsList;
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> apDoctorCol;
    @FXML private TableColumn<Appointment, String> apDateCol;
    @FXML private TableColumn<Appointment, String> apTimeCol;
    @FXML private TextField regNameField;
    @FXML private TextField regAgeField;
    @FXML private TextField regLocationField;
    @FXML private TextField regPhoneField;
    @FXML private ComboBox<String> regSexCombo;
    @FXML private TextField regUserField;
    @FXML private PasswordField regPassField;
    @FXML private Label registerFeedback;

    private final AppointmentService appointmentService = new AppointmentService();
    private Patient patient;

    @FXML
    public void initialize() {
        regSexCombo.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        timeCombo.setItems(FXCollections.observableArrayList(
                appointmentService.getDefaultSlots().stream().map(LocalTime::toString).collect(Collectors.toList())));
        doctorCombo.setItems(FXCollections.observableArrayList(
                HospitalContext.getInstance().getHospital().getDoctors().values().stream()
                        .map(d -> d.getDoctorId() + " - " + d.getName() + " (" + d.getSpecialization() + ")").toList()
        ));

        UserAccount user = SessionManager.getInstance().getCurrentUser();
        if (user != null && user.getRole() == Role.PATIENT) {
            patient = HospitalContext.getInstance().getHospital().getPatients().get(user.getLinkedId());
            patientLabel.setText("Patient: " + patient.getName());
            loadPatientViews();
        } else {
            patientLabel.setText("Guest mode: register a patient account");
        }

        apDoctorCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDoctorName()));
        apDateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getDate())));
        apTimeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getTimeValue())));
    }

    @FXML
    private void handleBook() {
        try {
            if (patient == null) throw new InvalidDataException("Login as patient first");
            String docEntry = doctorCombo.getValue();
            if (docEntry == null) throw new InvalidDataException("Select a doctor");
            String doctorId = docEntry.split(" - ")[0];
            LocalDate date = datePicker.getValue();
            LocalTime time = LocalTime.parse(timeCombo.getValue());
            appointmentService.bookAppointment(patient.getId(), doctorId, date, time);
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            bookingFeedback.setText("Appointment booked successfully");
            loadPatientViews();
        } catch (Exception e) {
            bookingFeedback.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        try {
            String id = HospitalContext.getInstance().getIdGenerator().nextPatientId();
            Patient p = new Patient(id, regNameField.getText(), Integer.parseInt(regAgeField.getText()), regLocationField.getText(), regPhoneField.getText(), regSexCombo.getValue());
            HospitalContext.getInstance().getHospital().registerPatient(p);
            HospitalContext.getInstance().getAuthService().createPatientAccount(p, regUserField.getText(), regPassField.getText());
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            registerFeedback.setText("Registration successful. Login from Login screen.");
        } catch (Exception e) {
            registerFeedback.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneNavigator.navigate("/fxml/Login.fxml", "Hospital Management System");
    }

    @FXML
    private void handleGoLogin() {
        SceneNavigator.navigate("/fxml/Login.fxml", "Hospital Management System");
    }

    private void loadPatientViews() {
        if (patient == null) return;
        Platform.runLater(() -> {
            recordsList.setItems(FXCollections.observableArrayList(
                    patient.getRecords().stream()
                            .map(r -> "Diagnosis: " + r.getDescription() + " | Prescription: " + r.getMedicine())
                            .toList()
            ));
            appointmentsTable.setItems(FXCollections.observableArrayList(
                    HospitalContext.getInstance().getHospital().getAppointmentsForPatient(patient.getId())
            ));
        });
    }
}
