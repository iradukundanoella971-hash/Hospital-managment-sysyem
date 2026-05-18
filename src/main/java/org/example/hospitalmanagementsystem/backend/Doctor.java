package org.example.hospitalmanagementsystem.backend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Doctor.java
 * Model class for a Doctor.
 * Uses JavaFX Property types so TableView can display data automatically.
 */
public class Doctor {

    private final SimpleStringProperty  doctorId;
    private final SimpleStringProperty  name;
    private final SimpleStringProperty  phoneNumber;
    private final SimpleStringProperty  specialization;
    private final SimpleStringProperty  location;
    private final SimpleIntegerProperty age;
    private final SimpleStringProperty  username;
    private final SimpleStringProperty  password;

    public Doctor(String doctorId, String name, String phoneNumber,
                  String specialization, String location, int age,
                  String username, String password) {
        this.doctorId       = new SimpleStringProperty(doctorId);
        this.name           = new SimpleStringProperty(name);
        this.phoneNumber    = new SimpleStringProperty(phoneNumber);
        this.specialization = new SimpleStringProperty(specialization);
        this.location       = new SimpleStringProperty(location);
        this.age            = new SimpleIntegerProperty(age);
        this.username       = new SimpleStringProperty(username);
        this.password       = new SimpleStringProperty(password);
    }

    public String getDoctorId()       { return doctorId.get(); }
    public String getName()           { return name.get(); }
    public String getPhoneNumber()    { return phoneNumber.get(); }
    public String getSpecialization() { return specialization.get(); }
    public String getLocation()       { return location.get(); }
    public int    getAge()            { return age.get(); }
    public String getUsername()       { return username.get(); }
    public String getPassword()       { return password.get(); }

    public SimpleStringProperty  doctorIdProperty()       { return doctorId; }
    public SimpleStringProperty  nameProperty()           { return name; }
    public SimpleStringProperty  phoneNumberProperty()    { return phoneNumber; }
    public SimpleStringProperty  specializationProperty() { return specialization; }
    public SimpleStringProperty  locationProperty()       { return location; }
    public SimpleIntegerProperty ageProperty()            { return age; }
    public SimpleStringProperty  usernameProperty()       { return username; }
}
