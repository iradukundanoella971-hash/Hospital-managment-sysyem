package org.example.hospitalmanagmentsystem.backend;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    private String doctorId;
    private String specialization;
    private String password;
    public List<Appointment> appointments = new ArrayList<>();

    public Doctor(String doctorId, String name, int age, String specialization, String password) {
        super(name, age);
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new InvalidDataException("Specialization cannot be empty");
        }
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.password = password;
    }

    public boolean login(String name, String password) {
        return getName().equals(name) && this.password.equals(password);
    }

    public String getSpecialization() { return specialization; }

    @Override
    void displayInfo() {
        System.out.println("Doctor: " + getName() + ", Specialization: " + specialization);
    }
}