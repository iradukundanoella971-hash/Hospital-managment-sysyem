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

public class PatientController implements Initializable {

    @FXML private TextField patientIdField, patientNameField, patientAgeField;
    @FXML private TextField locationField, phoneField, searchField;
    @FXML private ComboBox<String> sexCombo;
    @FXML private TableView<Patient> patientsTable;
    @FXML private TableColumn<Patient, String> colId, colName, colLocation, colPhone, colSex;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private Label patientStatusLabel, globalStatusLabel;

    private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private FilteredList<Patient> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        setupSexCombo();
        loadPatients();
        setupSearch();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
    }

    private void setupSexCombo() {
        sexCombo.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
    }

    private void loadPatients() {
        Platform.runLater(() -> {
            patientList.clear();
            patientList.addAll(MainViewController.getHospital().getPatients().values());
            filteredList = new FilteredList<>(patientList, p -> true);
            patientsTable.setItems(filteredList);
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, newVal) -> {
            filteredList.setPredicate(patient -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lowerCase = newVal.toLowerCase();
                return patient.getName().toLowerCase().contains(lowerCase) ||
                        patient.getId().toLowerCase().contains(lowerCase);
            });
        });
    }

    @FXML
    private void handleAddPatient() {
        try {
            String id = patientIdField.getText();
            String name = patientNameField.getText();
            int age = Integer.parseInt(patientAgeField.getText());
            String location = locationField.getText();
            String phone = phoneField.getText();
            String sex = sexCombo.getValue();

            if (sex == null) {
                AlertUtil.showError(patientStatusLabel, "Please select sex");
                return;
            }

            Patient patient = new Patient(id, name, age, location, phone, sex);
            MainViewController.getHospital().registerPatient(patient);

            loadPatients();
            clearPatientForm();
            AlertUtil.showSuccess(patientStatusLabel, "Patient registered: " + name);

        } catch (NumberFormatException e) {
            AlertUtil.showError(patientStatusLabel, "Invalid age format");
        } catch (InvalidDataException e) {
            AlertUtil.showError(patientStatusLabel, e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError(patientStatusLabel, "Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {}

    @FXML
    private void handleClearSearch() {
        searchField.clear();
    }

    private void clearPatientForm() {
        patientIdField.clear();
        patientNameField.clear();
        patientAgeField.clear();
        locationField.clear();
        phoneField.clear();
        sexCombo.setValue(null);
    }
}