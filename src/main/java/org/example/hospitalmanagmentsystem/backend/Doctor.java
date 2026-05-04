package org.example.hospitalmanagmentsystem.backend;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    private String doctorId;
    private String specialization;
    private String password;
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor(String doctorId, String name, int age, String specialization, String password) {
        super(name, age);
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new InvalidDataException("Specialization cannot be empty");
        }
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.password = password;
    }

    public String getDoctorId() { return doctorId; }
    public String getSpecialization() { return specialization; }
    public List<Appointment> getAppointments() { return appointments; }

    public boolean login(String name, String password) {
        return getName().equals(name) && this.password.equals(password);
    }

    public void addAppointment(Appointment appt) {
        if (appointments.size() >= 10) {
            throw new RuntimeException("Doctor not available today (max 10 patients)");
        }
        for (Appointment a : appointments) {
            if (a.getTime().equals(appt.getTime())) {
                throw new RuntimeException("Time already booked. Choose another time.");
            }
        }
        appointments.add(appt);
    }

    public void viewAppointments() {
        for (Appointment a : appointments) {
            a.showAppointment();
            System.out.println("-------------");
        }
    }

    @Override
    void displayInfo() {
        System.out.println("Doctor: " + getName() +
                ", Age: " + getAge() +
                ", Specialization: " + specialization);
    }
}