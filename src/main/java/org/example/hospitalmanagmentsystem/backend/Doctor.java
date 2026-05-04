package org.example.hospitalmanagmentsystem.backend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Doctor extends Person {
    private String doctorId;
    private String specialization;
    private String password;
    private List<Appointment> appointments = new ArrayList<>();
    private final Set<String> availableSlots = new HashSet<>();

    public Doctor(String doctorId, String name, int age, String specialization, String password) {
        super(name, age);
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new InvalidDataException("Specialization cannot be empty");
        }
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.password = password;
        seedDefaultSlots();
    }

    public String getDoctorId() { return doctorId; }
    public String getSpecialization() { return specialization; }
    public List<Appointment> getAppointments() { return appointments; }
    public Set<String> getAvailableSlots() { return availableSlots; }

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

    public boolean isAvailable(String slotKey) {
        if (!availableSlots.contains(slotKey)) {
            return false;
        }
        return appointments.stream().noneMatch(a -> {
            String booked = a.getDate() != null ? a.getDate() + "|" + a.getTime() : a.getTime();
            return booked.equals(slotKey);
        });
    }

    public void addAvailableSlot(String slotKey) {
        availableSlots.add(slotKey);
    }

    private void seedDefaultSlots() {
        // slot values are created dynamically by date in scheduling service.
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