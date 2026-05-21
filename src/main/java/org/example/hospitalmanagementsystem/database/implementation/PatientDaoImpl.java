package org.example.hospitalmanagementsystem.database.implementation;

import org.example.hospitalmanagementsystem.backend.Patient;
import org.example.hospitalmanagementsystem.database.Connect;
import org.example.hospitalmanagementsystem.database.dao.PatientDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PatientDaoImpl.java
 *
 * Implements PatientDao.
 * Contains all SQL operations for the patient table.
 */
public class PatientDaoImpl implements PatientDao {

    // ── SAVE (INSERT) ─────────────────────────────────────────────────────────

    /**
     * Inserts a new patient into the patient table.
     * Used when a patient signs up.
     */
    @Override
    public boolean save(Patient patient) {
        String query = """
            INSERT INTO patient (patient_id, name, location, username, password)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, patient.getPatientId());
            ps.setString(2, patient.getName());
            ps.setString(3, patient.getLocation());
            ps.setString(4, patient.getUsername());
            ps.setString(5, patient.getPassword());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("PatientDaoImpl.save() error: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @Override
    public boolean update(Patient patient) {
        String query = """
            UPDATE patient
            SET name=?, location=?, username=?, password=?
            WHERE patient_id=?
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, patient.getName());
            ps.setString(2, patient.getLocation());
            ps.setString(3, patient.getUsername());
            ps.setString(4, patient.getPassword());
            ps.setString(5, patient.getPatientId());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("PatientDaoImpl.update() error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Override
    public boolean delete(String patientId) {
        String query = "DELETE FROM patient WHERE patient_id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, patientId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("PatientDaoImpl.delete() error: " + e.getMessage());
            return false;
        }
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────

    @Override
    public Patient get(String patientId) {
        String query = "SELECT * FROM patient WHERE patient_id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapPatient(rs);

        } catch (SQLException e) {
            System.out.println("PatientDaoImpl.get() error: " + e.getMessage());
        }
        return null;
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────

    @Override
    public List<Patient> getAll() {
        List<Patient> list = new ArrayList<>();
        String query = "SELECT * FROM patient ORDER BY name";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapPatient(rs));
            }

        } catch (SQLException e) {
            System.out.println("PatientDaoImpl.getAll() error: " + e.getMessage());
        }
        return list;
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────

    @Override
    public boolean login(String username, String password) {
        String query = "SELECT id FROM patient WHERE username = ? AND password = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("PatientDaoImpl.login() error: " + e.getMessage());
            return false;
        }
    }

    // ── GET PATIENT ID BY USERNAME ────────────────────────────────────────────

    @Override
    public String getPatientIdByUsername(String username) {
        String query = "SELECT patient_id FROM patient WHERE username = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("patient_id");

        } catch (SQLException e) {
            System.out.println("PatientDaoImpl.getPatientIdByUsername() error: " + e.getMessage());
        }
        return null;
    }

    // ── GET PATIENTS BY DOCTOR ────────────────────────────────────────────────

    /**
     * Returns all patients who have at least one appointment with the given doctor.
     * Uses a JOIN between patient and appointment tables.
     */
    @Override
    public List<Patient> getPatientsByDoctor(String doctorId) {
        List<Patient> list = new ArrayList<>();
        String query = """
            SELECT DISTINCT p.patient_id, p.name, p.location, p.username, p.password
            FROM patient p
            JOIN appointment a ON p.patient_id = a.patient_id
            WHERE a.doctor_id = ?
            ORDER BY p.name
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPatient(rs));
            }

        } catch (SQLException e) {
            System.out.println("PatientDaoImpl.getPatientsByDoctor() error: " + e.getMessage());
        }
        return list;
    }

    // ── PRIVATE HELPER ────────────────────────────────────────────────────────

    private Patient mapPatient(ResultSet rs) throws SQLException {
        return new Patient(
            rs.getString("patient_id"),
            rs.getString("name"),
            rs.getString("location"),
            rs.getString("username"),
            rs.getString("password")
        );
    }
}
