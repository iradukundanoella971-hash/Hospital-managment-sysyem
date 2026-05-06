package org.example.hospitalmanagmentsystem.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hospitalmanagmentsystem.backend.Appointment;
import org.example.hospitalmanagmentsystem.backend.Doctor;
import org.example.hospitalmanagmentsystem.backend.Patient;
import org.example.hospitalmanagmentsystem.backend.UserAccount;
import org.example.hospitalmanagmentsystem.component.AppointmentService;
import org.example.hospitalmanagmentsystem.component.HospitalContext;
import org.example.hospitalmanagmentsystem.component.SessionManager;
import org.example.hospitalmanagmentsystem.ui.SceneNavigator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

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
    @FXML private TextField patientNameField;
    @FXML private TextField patientAgeField;
    @FXML private TextField patientLocationField;
    @FXML private TextField patientPhoneField;
    @FXML private ComboBox<String> patientSexCombo;
    @FXML private TextField patientUserField;
    @FXML private PasswordField patientPassField;
    @FXML private TextField adminUserField;
    @FXML private PasswordField adminPassField;
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> apIdCol;
    @FXML private TableColumn<Appointment, String> apDoctorCol;
    @FXML private TableColumn<Appointment, String> apPatientCol;
    @FXML private TableColumn<Appointment, String> apDateCol;
    @FXML private TableColumn<Appointment, String> apTimeCol;
    @FXML private TableColumn<Appointment, String> apStatusCol;
    @FXML private DatePicker rescheduleDatePicker;
    @FXML private ComboBox<String> rescheduleTimeCombo;
    @FXML private ComboBox<String> filterDoctorCombo;
    @FXML private ComboBox<String> filterStatusCombo;
    @FXML private DatePicker filterDatePicker;
    @FXML private TableView<UserAccount> usersTable;
    @FXML private TableColumn<UserAccount, String> userNameCol;
    @FXML private TableColumn<UserAccount, String> userRoleCol;
    @FXML private TableColumn<UserAccount, String> userLinkedCol;
    @FXML private TableColumn<UserAccount, String> userActiveCol;

    private final AppointmentService appointmentService = new AppointmentService();

    @FXML
    public void initialize() {
        doctorIdCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDoctorId()));
        doctorNameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        doctorSpecCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSpecialization()));
        patientIdCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
        patientNameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        patientSexCombo.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        rescheduleTimeCombo.setItems(FXCollections.observableArrayList(
                appointmentService.getDefaultSlots().stream().map(LocalTime::toString).toList()
        ));
        filterStatusCombo.setItems(FXCollections.observableArrayList("ALL", "BOOKED", "CANCELLED", "COMPLETED"));
        filterStatusCombo.setValue("ALL");
        apIdCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAppointmentId()));
        apDoctorCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDoctorName()));
        apPatientCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPatientName()));
        apDateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getDate())));
        apTimeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getTimeValue())));
        apStatusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
        userNameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));
        userRoleCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRole().name()));
        userLinkedCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLinkedId()));
        userActiveCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().isActive())));
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
        HospitalContext.getInstance().getAuthService().getUsers().values()
                .removeIf(u -> u.getLinkedId().equalsIgnoreCase(doctor.getDoctorId()));
        HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
        refresh();
    }

    @FXML
    private void handleAddPatient() {
        try {
            String id = HospitalContext.getInstance().getIdGenerator().nextPatientId();
            Patient patient = new Patient(
                    id,
                    patientNameField.getText(),
                    Integer.parseInt(patientAgeField.getText()),
                    patientLocationField.getText(),
                    patientPhoneField.getText(),
                    patientSexCombo.getValue()
            );
            HospitalContext.getInstance().getHospital().registerPatient(patient);
            HospitalContext.getInstance().getAuthService().createPatientAccount(patient, patientUserField.getText(), patientPassField.getText());
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            refresh();
        } catch (Exception e) {
            summaryLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddAdmin() {
        try {
            HospitalContext.getInstance().getAuthService().createAdmin(adminUserField.getText(), adminPassField.getText());
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            refresh();
        } catch (Exception e) {
            summaryLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeactivateUser() {
        UserAccount account = usersTable.getSelectionModel().getSelectedItem();
        if (account == null) {
            return;
        }
        HospitalContext.getInstance().getAuthService().deactivate(account.getUsername());
        HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
        refresh();
    }

    @FXML
    private void handleCancelAppointment() {
        Appointment ap = appointmentsTable.getSelectionModel().getSelectedItem();
        if (ap == null) return;
        try {
            appointmentService.cancelAppointment(ap.getAppointmentId());
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            refresh();
        } catch (Exception e) {
            summaryLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRescheduleAppointment() {
        Appointment ap = appointmentsTable.getSelectionModel().getSelectedItem();
        if (ap == null) return;
        try {
            appointmentService.rescheduleAppointment(
                    ap.getAppointmentId(),
                    rescheduleDatePicker.getValue(),
                    LocalTime.parse(rescheduleTimeCombo.getValue())
            );
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            refresh();
        } catch (Exception e) {
            summaryLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRemovePatient() {
        Patient patient = patientsTable.getSelectionModel().getSelectedItem();
        if (patient == null) return;
        HospitalContext.getInstance().getHospital().getPatients().remove(patient.getId());
        HospitalContext.getInstance().getAuthService().getUsers().values()
                .removeIf(u -> u.getLinkedId().equalsIgnoreCase(patient.getId()));
        HospitalContext.getInstance().getHospital().getAppointments().values()
                .removeIf(a -> a.getPatient().getId().equalsIgnoreCase(patient.getId()));
        HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
        refresh();
    }

    @FXML
    private void handleRefresh() {
        refresh();
    }

    @FXML
    private void handleApplyAppointmentFilter() {
        applyAppointmentFilter();
    }

    @FXML
    private void handleClearAppointmentFilter() {
        filterDatePicker.setValue(null);
        filterStatusCombo.setValue("ALL");
        filterDoctorCombo.setValue(null);
        applyAppointmentFilter();
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
            ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentService.getAllAppointmentsSorted());
            ObservableList<UserAccount> users = FXCollections.observableArrayList(HospitalContext.getInstance().getAuthService().getUsers().values());
            filterDoctorCombo.setItems(FXCollections.observableArrayList(
                    doctors.stream().map(d -> d.getDoctorId() + " - " + d.getName()).collect(Collectors.toList())
            ));
            doctors.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            patients.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            doctorsTable.setItems(doctors);
            patientsTable.setItems(patients);
            appointmentsTable.setItems(appointments);
            usersTable.setItems(users);
            summaryLabel.setText("Doctors: " + doctors.size() + " | Patients: " + patients.size() + " | Appointments: "
                    + HospitalContext.getInstance().getHospital().getAppointments().size());
        });
    }

    private void applyAppointmentFilter() {
        String selectedDoctor = filterDoctorCombo.getValue();
        String doctorId = selectedDoctor == null ? null : selectedDoctor.split(" - ")[0];
        String status = filterStatusCombo.getValue();
        LocalDate date = filterDatePicker.getValue();

        var filtered = appointmentService.getAllAppointmentsSorted().stream()
                .filter(a -> doctorId == null || a.getDoctor().getDoctorId().equalsIgnoreCase(doctorId))
                .filter(a -> date == null || date.equals(a.getDate()))
                .filter(a -> status == null || "ALL".equalsIgnoreCase(status) || a.getStatus().equalsIgnoreCase(status))
                .toList();
        appointmentsTable.setItems(FXCollections.observableArrayList(filtered));
    }
}
