package org.example.hospitalmanagmentsystem.backend;

public class Appointment {
    private Doctor doctor;
    private Patient patient;
    private String time;

    public Appointment(Doctor doctor, Patient patient, String time) {
        if (doctor == null) throw new InsufficientDoctorException("Doctor required");
        if (patient == null) throw new InvalidDataException("Patient required");
        this.doctor = doctor;
        this.patient = patient;
        this.time = time;
    }

    public String getTime() { return time; }
    public Patient getPatient() { return patient; }
    public void showAppointment() {
        System.out.println("Time: " + time + " | Doctor: " + doctor.getName() + " | Patient: " + patient.getName());
    }
}