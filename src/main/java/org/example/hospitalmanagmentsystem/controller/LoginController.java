package org.example.hospitalmanagmentsystem.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.hospitalmanagmentsystem.backend.Doctor;
import org.example.hospitalmanagmentsystem.backend.InvalidDataException;
import org.example.hospitalmanagmentsystem.backend.Patient;
import org.example.hospitalmanagmentsystem.backend.Role;
import org.example.hospitalmanagmentsystem.backend.UserAccount;
import org.example.hospitalmanagmentsystem.component.AlertUtil;
import org.example.hospitalmanagmentsystem.component.HospitalContext;
import org.example.hospitalmanagmentsystem.component.SessionManager;
import org.example.hospitalmanagmentsystem.ui.SceneNavigator;

public class LoginController {
    @FXML private ComboBox<String> loginRoleCombo;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> registerRoleCombo;
    @FXML private TextField registerNameField;
    @FXML private TextField registerAgeField;
    @FXML private TextField registerSpecializationField;
    @FXML private TextField registerLocationField;
    @FXML private TextField registerPhoneField;
    @FXML private ComboBox<String> registerSexCombo;
    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private Label feedbackLabel;

    @FXML
    public void initialize() {
        loginRoleCombo.setItems(FXCollections.observableArrayList("ADMIN", "DOCTOR", "PATIENT"));
        registerRoleCombo.setItems(FXCollections.observableArrayList("ADMIN", "DOCTOR", "PATIENT"));
        registerSexCombo.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
    }

    @FXML
    private void handleLogin() {
        try {
            Role selectedRole = parseRole(loginRoleCombo.getValue());
            UserAccount user = HospitalContext.getInstance()
                    .getAuthService()
                    .login(usernameField.getText(), passwordField.getText(), selectedRole);
            SessionManager.getInstance().login(user);
            openDashboard(user.getRole());
        } catch (Exception e) {
            AlertUtil.showError(feedbackLabel, e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        try {
            Role role = parseRole(registerRoleCombo.getValue());
            String username = registerUsernameField.getText();
            String password = registerPasswordField.getText();
            if (role == Role.ADMIN) {
                HospitalContext.getInstance().getAuthService().createAdmin(username, password);
            } else if (role == Role.DOCTOR) {
                String name = requireText(registerNameField.getText(), "Doctor name is required");
                int age = parseAge(registerAgeField.getText());
                String specialization = requireText(registerSpecializationField.getText(), "Specialization is required");
                String doctorId = HospitalContext.getInstance().getIdGenerator().nextDoctorId();
                Doctor doctor = new Doctor(doctorId, name, age, specialization, password);
                HospitalContext.getInstance().getHospital().registerDoctor(doctor);
                HospitalContext.getInstance().getAuthService().createDoctorAccount(doctor, username, password);
            } else {
                String name = requireText(registerNameField.getText(), "Patient name is required");
                int age = parseAge(registerAgeField.getText());
                String location = requireText(registerLocationField.getText(), "Location is required");
                String phone = requireText(registerPhoneField.getText(), "Phone is required");
                String sex = requireText(registerSexCombo.getValue(), "Sex is required");
                String patientId = HospitalContext.getInstance().getIdGenerator().nextPatientId();
                Patient patient = new Patient(patientId, name, age, location, phone, sex);
                HospitalContext.getInstance().getHospital().registerPatient(patient);
                HospitalContext.getInstance().getAuthService().createPatientAccount(patient, username, password);
            }
            HospitalContext.getInstance().getPersistenceService()
                    .save(HospitalContext.getInstance().getHospital(), HospitalContext.getInstance().getAuthService());
            clearRegisterFields();
            AlertUtil.showSuccess(feedbackLabel, "Account created. You can login now.");
        } catch (Exception e) {
            AlertUtil.showError(feedbackLabel, e.getMessage());
        }
    }

    private void openDashboard(Role role) {
        if (role == Role.ADMIN) {
            SceneNavigator.navigate("/fxml/AdminDashboard.fxml", "Admin Dashboard");
            return;
        }
        if (role == Role.DOCTOR) {
            SceneNavigator.navigate("/fxml/DoctorDashboard.fxml", "Doctor Dashboard");
            return;
        }
        SceneNavigator.navigate("/fxml/PatientDashboard.fxml", "Patient Dashboard");
    }

    private Role parseRole(String roleText) {
        if (roleText == null || roleText.isBlank()) {
            throw new InvalidDataException("Please select a role");
        }
        return Role.valueOf(roleText.toUpperCase());
    }

    private int parseAge(String ageText) {
        String cleaned = requireText(ageText, "Age is required");
        try {
            int age = Integer.parseInt(cleaned);
            if (age <= 0) {
                throw new InvalidDataException("Age must be greater than 0");
            }
            return age;
        } catch (NumberFormatException ex) {
            throw new InvalidDataException("Age must be a number");
        }
    }

    private String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new InvalidDataException(message);
        }
        return value.trim();
    }

    private void clearRegisterFields() {
        registerNameField.clear();
        registerAgeField.clear();
        registerSpecializationField.clear();
        registerLocationField.clear();
        registerPhoneField.clear();
        registerSexCombo.setValue(null);
        registerUsernameField.clear();
        registerPasswordField.clear();
    }
}
