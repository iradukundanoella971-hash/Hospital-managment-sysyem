package org.example.hospitalmanagementsystem.backend;

import javafx.beans.property.SimpleStringProperty;

public class Patient {

    private final SimpleStringProperty patientId;
    private final SimpleStringProperty name;
    private final SimpleStringProperty location;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;

    public Patient(String patientId, String name, String location,
                   String username, String password) {
        this.patientId = new SimpleStringProperty(patientId);
        this.name      = new SimpleStringProperty(name);
        this.location  = new SimpleStringProperty(location);
        this.username  = new SimpleStringProperty(username);
        this.password  = new SimpleStringProperty(password);
    }

    public String getPatientId() { return patientId.get(); }
    public String getName()      { return name.get(); }
    public String getLocation()  { return location.get(); }
    public String getUsername()  { return username.get(); }
    public String getPassword()  { return password.get(); }

    public SimpleStringProperty patientIdProperty() { return patientId; }
    public SimpleStringProperty nameProperty()      { return name; }
    public SimpleStringProperty locationProperty()  { return location; }
    public SimpleStringProperty usernameProperty()  { return username; }
}
