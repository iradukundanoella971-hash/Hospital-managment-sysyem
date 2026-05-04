package org.example.hospitalmanagmentsystem.backend;

import java.util.HashMap;
import java.util.Map;

public class Hospital {
    private Map<String, Doctor> doctors = new HashMap<>();
    private Map<String, Patient> patients = new HashMap<>();

    public void registerDoctor(Doctor doctor) {
        doctors.put(doctor.getName(), doctor);
    }

    public void registerPatient(Patient patient) {
        patients.put(patient.getName(), patient);
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
            throw new RuntimeException("No doctor available for this specialization");
        }
        Appointment appt = new Appointment(doctor, patient, time);
        doctor.addAppointment(appt);
    }

    public Map<String, Doctor> getDoctors() { return doctors; }
    public Map<String, Patient> getPatients() { return patients; }
}
