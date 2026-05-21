package org.example.hospitalmanagementsystem.database.implementation;

import org.example.hospitalmanagementsystem.backend.Appointment;
import org.example.hospitalmanagementsystem.database.Connect;
import org.example.hospitalmanagementsystem.database.dao.AppointmentDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentDaoImpl.java
 *
 * Implements AppointmentDao.
 * Contains all SQL operations for the appointment table.
 */
public class AppointmentDaoImpl implements AppointmentDao {

    // ── SAVE (INSERT) ─────────────────────────────────────────────────────────

    /**
     * Inserts a new appointment directly from an Appointment object.
     * For booking with duplicate check, use book() instead.
     */
    @Override
    public boolean save(Appointment appointment) {
        String query = """
            INSERT INTO appointment (doctor_id, patient_id, appointment_date, appointment_time, status)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, appointment.getDoctorId());
            ps.setString(2, appointment.getPatientId());
            ps.setDate(3, Date.valueOf(appointment.getDate()));
            ps.setTime(4, Time.valueOf(appointment.getTime()));
            ps.setString(5, appointment.getStatus());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.save() error: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @Override
    public boolean update(Appointment appointment) {
        String query = """
            UPDATE appointment
            SET doctor_id=?, patient_id=?, appointment_date=?, appointment_time=?, status=?
            WHERE id=?
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, appointment.getDoctorId());
            ps.setString(2, appointment.getPatientId());
            ps.setDate(3, Date.valueOf(appointment.getDate()));
            ps.setTime(4, Time.valueOf(appointment.getTime()));
            ps.setString(5, appointment.getStatus());
            ps.setInt(6, appointment.getId());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.update() error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM appointment WHERE id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.delete() error: " + e.getMessage());
            return false;
        }
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────

    @Override
    public Appointment get(Integer id) {
        String query = "SELECT * FROM appointment WHERE id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapAppointment(rs);

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.get() error: " + e.getMessage());
        }
        return null;
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────

    @Override
    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointment ORDER BY appointment_date, appointment_time";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapAppointment(rs));
            }

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.getAll() error: " + e.getMessage());
        }
        return list;
    }

    // ── GET BY DOCTOR ─────────────────────────────────────────────────────────

    @Override
    public List<Appointment> getByDoctor(String doctorId) {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointment WHERE doctor_id = ? ORDER BY appointment_date, appointment_time";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.getByDoctor() error: " + e.getMessage());
        }
        return list;
    }

    // ── GET BY PATIENT ────────────────────────────────────────────────────────

    @Override
    public List<Appointment> getByPatient(String patientId) {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointment WHERE patient_id = ? ORDER BY appointment_date, appointment_time";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.getByPatient() error: " + e.getMessage());
        }
        return list;
    }

    // ── GET APPOINTMENT IDs BY DOCTOR ─────────────────────────────────────────

    @Override
    public List<String> getAppointmentIdsByDoctor(String doctorId) {
        List<String> ids = new ArrayList<>();
        String query = "SELECT id FROM appointment WHERE doctor_id = ? ORDER BY id";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(String.valueOf(rs.getInt("id")));
            }

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.getAppointmentIdsByDoctor() error: " + e.getMessage());
        }
        return ids;
    }

    // ── GET ALL DOCTOR IDs ────────────────────────────────────────────────────

    @Override
    public List<String> getAllDoctorIds() {
        List<String> ids = new ArrayList<>();
        String query = "SELECT doctor_id FROM doctor ORDER BY doctor_id";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getString("doctor_id"));
            }

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.getAllDoctorIds() error: " + e.getMessage());
        }
        return ids;
    }

    // ── GET PATIENT ID BY APPOINTMENT ─────────────────────────────────────────

    @Override
    public String getPatientIdByAppointment(int appointmentId) {
        String query = "SELECT patient_id FROM appointment WHERE id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("patient_id");

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.getPatientIdByAppointment() error: " + e.getMessage());
        }
        return null;
    }

    // ── BOOK (with duplicate check) ───────────────────────────────────────────

    /**
     * Books a new appointment.
     * First checks if the same booking already exists.
     * Returns true if booked, false if duplicate.
     */
    @Override
    public boolean book(String doctorId, String patientId, String date, String time) {
        // Step 1: Check for duplicate
        String dupCheck = """
            SELECT id FROM appointment
            WHERE doctor_id=? AND patient_id=? AND appointment_date=? AND appointment_time=?
        """;

        try (Connection conn = Connect.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(dupCheck)) {
                ps.setString(1, doctorId);
                ps.setString(2, patientId);
                ps.setDate(3, Date.valueOf(date));
                ps.setTime(4, Time.valueOf(time + ":00"));

                if (ps.executeQuery().next()) {
                    return false; // already booked
                }
            }

            // Step 2: Insert the appointment
            String insert = """
                INSERT INTO appointment (doctor_id, patient_id, appointment_date, appointment_time)
                VALUES (?, ?, ?, ?)
            """;

            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, doctorId);
                ps.setString(2, patientId);
                ps.setDate(3, Date.valueOf(date));
                ps.setTime(4, Time.valueOf(time + ":00"));
                ps.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            System.out.println("AppointmentDaoImpl.book() error: " + e.getMessage());
            return false;
        }
    }

    // ── PRIVATE HELPER ────────────────────────────────────────────────────────

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
            rs.getInt("id"),
            rs.getString("doctor_id"),
            rs.getString("patient_id"),
            rs.getString("appointment_date"),
            rs.getString("appointment_time"),
            rs.getString("status")
        );
    }
}
