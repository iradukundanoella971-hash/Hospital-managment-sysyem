package org.example.hospitalmanagmentsystem.backend;


import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private String appointmentId;
    private Doctor doctor;
    private Patient patient;
    private String time;
    private LocalDate date;
    private LocalTime timeValue;
    private String status;
    private String followUpNotes;

    public Appointment(Doctor doctor, Patient patient, String time) {
        if (doctor == null) {
            throw new InsufficientDoctorException("Doctor required");
        }
        if (patient == null) {
            throw new InvalidDataException("Patient required");
        }
        this.doctor = doctor;
        this.patient = patient;
        this.time = time;
        this.status = "BOOKED";
    }

    public Appointment(String appointmentId, Doctor doctor, Patient patient, LocalDate date, LocalTime timeValue) {
        if (doctor == null) {
            throw new InsufficientDoctorException("Doctor required");
        }
        if (patient == null) {
            throw new InvalidDataException("Patient required");
        }
        if (date == null || timeValue == null) {
            throw new InvalidDataException("Date and time are required");
        }
        this.appointmentId = appointmentId;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.timeValue = timeValue;
        this.time = timeValue.toString();
        this.status = "BOOKED";
    }

    public String getTime() { return time; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public String getAppointmentId() { return appointmentId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTimeValue() { return timeValue; }
    public String getStatus() { return status; }
    public String getFollowUpNotes() { return followUpNotes; }
    public String getPatientName() { return patient.getName(); }
    public String getDoctorName() { return doctor.getName(); }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFollowUpNotes(String followUpNotes) {
        this.followUpNotes = followUpNotes;
    }

    public void showAppointment() {
        System.out.println("Time: " + time);
        System.out.println("Doctor: " + doctor.getName());
        System.out.println("Patient: " + patient.getName());
    }
}
