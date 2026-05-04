package org.example.hospitalmanagmentsystem.backend;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Hospital {
    private final Map<String, Doctor> doctors = new LinkedHashMap<>();
    private final Map<String, Patient> patients = new LinkedHashMap<>();
    private final Map<String, Appointment> appointments = new LinkedHashMap<>();

    public void registerDoctor(Doctor doctor) {
        doctors.put(doctor.getDoctorId(), doctor);
    }

    public void registerPatient(Patient patient) {
        patients.put(patient.getId(), patient);
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
        appointments.put(String.valueOf(appointments.size() + 1), appt);
    }

    public Map<String, Doctor> getDoctors() { return doctors; }
    public Map<String, Patient> getPatients() { return patients; }
    public Map<String, Appointment> getAppointments() { return appointments; }

    public Doctor getDoctorByName(String name) {
        return doctors.values().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Patient getPatientByName(String name) {
        return patients.values().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        return appointments.values().stream()
                .filter(a -> a.getDoctor().getDoctorId().equalsIgnoreCase(doctorId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsForPatient(String patientId) {
        return appointments.values().stream()
                .filter(a -> a.getPatient().getId().equalsIgnoreCase(patientId))
                .collect(Collectors.toList());
    }
}
