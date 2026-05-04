package org.example.hospitalmanagmentsystem.component;

import org.example.hospitalmanagmentsystem.backend.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PersistenceService {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.csv");
    private static final Path DOCTORS_FILE = DATA_DIR.resolve("doctors.csv");
    private static final Path PATIENTS_FILE = DATA_DIR.resolve("patients.csv");
    private static final Path APPOINTMENTS_FILE = DATA_DIR.resolve("appointments.csv");
    private static final Path RECORDS_FILE = DATA_DIR.resolve("records.csv");

    public void load(Hospital hospital, AuthService authService) {
        try {
            Files.createDirectories(DATA_DIR);
            loadDoctors(hospital);
            loadPatients(hospital);
            loadUsers(authService);
            loadAppointments(hospital);
            loadRecords(hospital);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data", e);
        }
    }

    public void save(Hospital hospital, AuthService authService) {
        try {
            Files.createDirectories(DATA_DIR);
            saveDoctors(hospital);
            savePatients(hospital);
            saveUsers(authService);
            saveAppointments(hospital);
            saveRecords(hospital);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data", e);
        }
    }

    private void loadDoctors(Hospital hospital) throws IOException {
        if (!Files.exists(DOCTORS_FILE)) return;
        for (String line : Files.readAllLines(DOCTORS_FILE)) {
            String[] p = line.split(",", -1);
            if (p.length < 5) continue;
            hospital.registerDoctor(new Doctor(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4]));
        }
    }

    private void loadPatients(Hospital hospital) throws IOException {
        if (!Files.exists(PATIENTS_FILE)) return;
        for (String line : Files.readAllLines(PATIENTS_FILE)) {
            String[] p = line.split(",", -1);
            if (p.length < 6) continue;
            hospital.registerPatient(new Patient(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], p[5]));
        }
    }

    private void loadUsers(AuthService authService) throws IOException {
        if (!Files.exists(USERS_FILE)) return;
        for (String line : Files.readAllLines(USERS_FILE)) {
            String[] p = line.split(",", -1);
            if (p.length < 5) continue;
            UserAccount acc = new UserAccount(p[0], p[1], Role.valueOf(p[2]), p[3], Boolean.parseBoolean(p[4]));
            authService.getUsers().put(acc.getUsername(), acc);
        }
    }

    private void loadAppointments(Hospital hospital) throws IOException {
        if (!Files.exists(APPOINTMENTS_FILE)) return;
        for (String line : Files.readAllLines(APPOINTMENTS_FILE)) {
            String[] p = line.split(",", -1);
            if (p.length < 6) continue;
            Doctor doctor = hospital.getDoctors().get(p[1]);
            Patient patient = hospital.getPatients().get(p[2]);
            if (doctor == null || patient == null) continue;
            Appointment a = new Appointment(p[0], doctor, patient, LocalDate.parse(p[3]), LocalTime.parse(p[4]));
            a.setStatus(p[5]);
            if (p.length > 6) a.setFollowUpNotes(p[6]);
            doctor.addAppointment(a);
            hospital.getAppointments().put(a.getAppointmentId(), a);
        }
    }

    private void loadRecords(Hospital hospital) throws IOException {
        if (!Files.exists(RECORDS_FILE)) return;
        for (String line : Files.readAllLines(RECORDS_FILE)) {
            String[] p = line.split(",", -1);
            if (p.length < 3) continue;
            Patient patient = hospital.getPatients().get(p[0]);
            if (patient != null) {
                patient.addRecord(new MedicalRecord(p[1], p[2]));
            }
        }
    }

    private void saveDoctors(Hospital hospital) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Doctor d : hospital.getDoctors().values()) {
            lines.add(String.join(",", d.getDoctorId(), d.getName(), String.valueOf(d.getAge()), d.getSpecialization(), "doctor123"));
        }
        Files.write(DOCTORS_FILE, lines);
    }

    private void savePatients(Hospital hospital) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Patient p : hospital.getPatients().values()) {
            lines.add(String.join(",", p.getId(), p.getName(), String.valueOf(p.getAge()), p.getLocation(), p.getPhone(), p.getSex()));
        }
        Files.write(PATIENTS_FILE, lines);
    }

    private void saveUsers(AuthService authService) throws IOException {
        List<String> lines = new ArrayList<>();
        for (UserAccount u : authService.getUsers().values()) {
            lines.add(String.join(",", u.getUsername(), u.getPasswordHash(), u.getRole().name(), u.getLinkedId(), String.valueOf(u.isActive())));
        }
        Files.write(USERS_FILE, lines);
    }

    private void saveAppointments(Hospital hospital) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Appointment a : hospital.getAppointments().values()) {
            lines.add(String.join(",",
                    a.getAppointmentId(),
                    a.getDoctor().getDoctorId(),
                    a.getPatient().getId(),
                    String.valueOf(a.getDate()),
                    String.valueOf(a.getTimeValue()),
                    a.getStatus(),
                    a.getFollowUpNotes() == null ? "" : a.getFollowUpNotes().replace(",", " ")
            ));
        }
        Files.write(APPOINTMENTS_FILE, lines);
    }

    private void saveRecords(Hospital hospital) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Patient p : hospital.getPatients().values()) {
            for (MedicalRecord r : p.getRecords()) {
                lines.add(String.join(",", p.getId(), r.getDescription().replace(",", " "), r.getMedicine().replace(",", " ")));
            }
        }
        Files.write(RECORDS_FILE, lines);
    }
}
