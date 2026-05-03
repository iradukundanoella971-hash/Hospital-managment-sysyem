package org.example.hospitalmanagmentsystem.backend;

import java.util.Map;

public class Hospital package org.example.hospitalmanagmentsystem.backend;

import java.util.HashMap;
import java.util.Map;

public class Hospital {
    public Map<String, Doctor> doctors = new HashMap<>();
    public Map<String, Patient> patients = new HashMap<>();

    public void registerDoctor(Doctor doctor) {
        doctors.put(doctor.getName(), doctor);
        System.out.println("Doctor registered: " + doctor.getName());
    }

    public void registerPatient(Patient patient) {
        patients.put(patient.getName(), patient);
        System.out.println("Patient registered: " + patient.getName());
    }

    public Doctor findDoctorBySpecialization(String spec) {
        for (Doctor d : doctors.values()) {
            if (d.getSpecialization().equalsIgnoreCase(spec)) {
                return d;
            }
        }
        return null;
    }

    public void bookAppointment(String spec, Patient patient, String time) {
        Doctor doctor = findDoctorBySpecialization(spec);
        if (doctor == null) {
            throw new RuntimeException("No doctor available for " + spec);
        }
        Appointment appt = new Appointment(doctor, patient, time);
        doctor.appointments.add(appt);
        System.out.println("Appointment booked: " + time + " with Dr. " + doctor.getName());
    }
}{
}
