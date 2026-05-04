package org.example.hospitalmanagmentsystem.controller;

import org.example.hospitalmanagmentsystem.backend.*;
import org.example.hospitalmanagmentsystem.component.AlertUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {

    @FXML private TextField doctorIdField, doctorNameField, doctorAgeField, searchField;
    @FXML private ComboBox<String> specializationCombo;
    @FXML private PasswordField doctorPasswordField;
    @FXML private TableView<Doctor> doctorsTable;
    @FXML private TableColumn<Doctor, String> colId, colName, colSpecialization;
    @FXML private TableColumn<Doctor, Integer> colAge;
    @FXML private Label doctorStatusLabel, globalStatusLabel;

    private ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private FilteredList<Doctor> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        setupSpecializations();
        loadDoctors();
        setupSearch();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
    }

    private void setupSpecializations() {
        specializationCombo.setItems(FXCollections.observableArrayList(
                "Cardiology", "Neurology", "Pediatrics", "Orthopedics",
                "Dermatology", "Radiology", "Emergency Medicine", "Oncology"
        ));
    }

    private void loadDoctors() {
        Platform.runLater(() -> {
            doctorList.clear();
            doctorList.addAll(MainViewController.getHospital().getDoctors().values());
            filteredList = new FilteredList<>(doctorList, p -> true);
            doctorsTable.setItems(filteredList);
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, newVal) -> {
            filteredList.setPredicate(doctor -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lowerCase = newVal.toLowerCase();
                return doctor.getName().toLowerCase().contains(lowerCase) ||
                        doctor.getSpecialization().toLowerCase().contains(lowerCase);
            });
        });
    }

    @FXML
    private void handleAddDoctor() {
        try {
            String id = doctorIdField.getText();
            String name = doctorNameField.getText();
            int age = Integer.parseInt(doctorAgeField.getText());
            String specialization = specializationCombo.getValue();
            String password = doctorPasswordField.getText();

            if (specialization == null) {
                AlertUtil.showError(doctorStatusLabel, "Please select specialization");
                return;
            }

            Doctor doctor = new Doctor(id, name, age, specialization, password);
            MainViewController.getHospital().registerDoctor(doctor);

            loadDoctors();
            clearDoctorForm();
            AlertUtil.showSuccess(doctorStatusLabel, "Doctor registered: " + name);

        } catch (NumberFormatException e) {
            AlertUtil.showError(doctorStatusLabel, "Invalid age format");
        } catch (InvalidDataException e) {
            AlertUtil.showError(doctorStatusLabel, e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError(doctorStatusLabel, "Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        // Search is handled by the listener
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
    }

    private void clearDoctorForm() {
        doctorIdField.clear();
        doctorNameField.clear();
        doctorAgeField.clear();
        specializationCombo.setValue(null);
        doctorPasswordField.clear();
    }
}
