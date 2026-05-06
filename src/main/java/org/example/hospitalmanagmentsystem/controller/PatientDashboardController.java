package org.example.hospitalmanagmentsystem.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hospitalmanagmentsystem.backend.*;
import org.example.hospitalmanagmentsystem.component.*;
import org.example.hospitalmanagmentsystem.ui.SceneNavigator;
import javafx.util.Duration;

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
    @FXML private DatePicker rescheduleDatePicker;
    @FXML private ComboBox<String> rescheduleTimeCombo;

    private final AppointmentService appointmentService = new AppointmentService();
    private Patient patient;
    private Timeline refreshTimeline;

    @FXML
    public void initialize() {
        apDoctorCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDoctorName()));
        apDateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getDate())));
        apTimeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getTimeValue())));

        timeCombo.setItems(FXCollections.observableArrayList(
                appointmentService.getDefaultSlots().stream().map(LocalTime::toString).collect(Collectors.toList())));
        rescheduleTimeCombo.setItems(FXCollections.observableArrayList(
                appointmentService.getDefaultSlots().stream().map(LocalTime::toString).collect(Collectors.toList())));
        doctorCombo.setItems(FXCollections.observableArrayList(
                HospitalContext.getInstance().getHospital().getDoctors().values().stream()
                        .map(d -> d.getDoctorId() + " - " + d.getName() + " (" + d.getSpecialization() + ")").toList()
        ));

        UserAccount user = SessionManager.getInstance().getCurrentUser();
        if (user != null && user.getRole() == Role.PATIENT) {
            patient = resolvePatientForUser(user);
            if (patient != null) {
                patientLabel.setText("Patient: " + patient.getName());
                loadPatientViews();
                startAutoRefresh();
            } else {
                patientLabel.setText("Patient account not linked. Please contact admin.");
            }
        } else {
            patientLabel.setText("Guest mode: register a patient account");
        }
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
    private void handleLogout() {
        stopAutoRefresh();
        SessionManager.getInstance().logout();
        SceneNavigator.navigate("/fxml/Login.fxml", "Hospital Management System");
    }

    @FXML
    private void handleCancelSelected() {
        try {
            Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new InvalidDataException("Select an appointment first");
            }
            appointmentService.cancelAppointment(selected.getAppointmentId());
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            bookingFeedback.setText("Appointment cancelled");
            loadPatientViews();
        } catch (Exception e) {
            bookingFeedback.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRescheduleSelected() {
        try {
            Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new InvalidDataException("Select an appointment first");
            }
            appointmentService.rescheduleAppointment(
                    selected.getAppointmentId(),
                    rescheduleDatePicker.getValue(),
                    LocalTime.parse(rescheduleTimeCombo.getValue())
            );
            HospitalContext.getInstance().getPersistenceService().save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            bookingFeedback.setText("Appointment rescheduled");
            loadPatientViews();
        } catch (Exception e) {
            bookingFeedback.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleGoLogin() {
        stopAutoRefresh();
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
            appointmentsTable.refresh();
        });
    }

    private Patient resolvePatientForUser(UserAccount user) {
        Patient byId = HospitalContext.getInstance().getHospital().getPatients().get(user.getLinkedId());
        if (byId != null) {
            return byId;
        }
        return HospitalContext.getInstance().getHospital().getPatientByName(user.getLinkedId());
    }

    private void startAutoRefresh() {
        stopAutoRefresh();
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(4), event -> loadPatientViews()));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void stopAutoRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
            refreshTimeline = null;
        }
    }
}
