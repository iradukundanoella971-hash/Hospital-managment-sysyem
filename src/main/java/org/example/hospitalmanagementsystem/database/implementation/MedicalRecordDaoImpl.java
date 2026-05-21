package org.example.hospitalmanagementsystem.database.implementation;

import org.example.hospitalmanagementsystem.backend.MedicalRecord;
import org.example.hospitalmanagementsystem.database.Connect;
import org.example.hospitalmanagementsystem.database.dao.MedicalRecordDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MedicalRecordDaoImpl.java
 *
 * Implements MedicalRecordDao.
 * Contains all SQL operations for the medical_record table.
 */
public class MedicalRecordDaoImpl implements MedicalRecordDao {

    // ── SAVE (INSERT) ─────────────────────────────────────────────────────────

    @Override
    public boolean save(MedicalRecord record) {
        String query = """
            INSERT INTO medical_record
            (appointment_id, doctor_id, patient_id, diagnosis, medicine,
             description, treatment_notes, follow_up_note, next_appointment)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, record.getAppointmentId());
            ps.setString(2, record.getDoctorId());
            ps.setString(3, record.getPatientId());
            ps.setString(4, record.getDiagnosis());
            ps.setString(5, record.getMedicine());
            ps.setString(6, record.getDescription());
            ps.setString(7, record.getTreatmentNotes());
            ps.setString(8, record.getFollowUpNote());
            setDateOrNull(ps, 9, record.getNextAppointment());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("MedicalRecordDaoImpl.save() error: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @Override
    public boolean update(MedicalRecord record) {
        String query = """
            UPDATE medical_record
            SET diagnosis=?, medicine=?, description=?, treatment_notes=?, follow_up_note=?, next_appointment=?
            WHERE appointment_id=?
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, record.getDiagnosis());
            ps.setString(2, record.getMedicine());
            ps.setString(3, record.getDescription());
            ps.setString(4, record.getTreatmentNotes());
            ps.setString(5, record.getFollowUpNote());
            setDateOrNull(ps, 6, record.getNextAppointment());
            ps.setInt(7, record.getAppointmentId());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("MedicalRecordDaoImpl.update() error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM medical_record WHERE id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("MedicalRecordDaoImpl.delete() error: " + e.getMessage());
            return false;
        }
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────

    @Override
    public MedicalRecord get(Integer id) {
        String query = "SELECT * FROM medical_record WHERE id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRecord(rs);

        } catch (SQLException e) {
            System.out.println("MedicalRecordDaoImpl.get() error: " + e.getMessage());
        }
        return null;
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────

    @Override
    public List<MedicalRecord> getAll() {
        List<MedicalRecord> list = new ArrayList<>();
        String query = "SELECT * FROM medical_record ORDER BY id DESC";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRecord(rs));
            }

        } catch (SQLException e) {
            System.out.println("MedicalRecordDaoImpl.getAll() error: " + e.getMessage());
        }
        return list;
    }

    // ── GET BY DOCTOR ─────────────────────────────────────────────────────────

    @Override
    public List<MedicalRecord> getByDoctor(String doctorId) {
        List<MedicalRecord> list = new ArrayList<>();
        String query = "SELECT * FROM medical_record WHERE doctor_id = ? ORDER BY id DESC";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRecord(rs));
            }

        } catch (SQLException e) {
            System.out.println("MedicalRecordDaoImpl.getByDoctor() error: " + e.getMessage());
        }
        return list;
    }

    // ── GET BY PATIENT ────────────────────────────────────────────────────────

    @Override
    public List<MedicalRecord> getByPatient(String patientId) {
        List<MedicalRecord> list = new ArrayList<>();
        String query = "SELECT * FROM medical_record WHERE patient_id = ? ORDER BY id DESC";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRecord(rs));
            }

        } catch (SQLException e) {
            System.out.println("MedicalRecordDaoImpl.getByPatient() error: " + e.getMessage());
        }
        return list;
    }

    // ── SAVE OR UPDATE ────────────────────────────────────────────────────────

    /**
     * If a record already exists for this appointment → UPDATE it.
     * If no record exists yet → INSERT a new one.
     *
     * This is the main method called by DoctorController when saving a record.
     */
    @Override
    public boolean saveOrUpdate(int appointmentId, String doctorId, String patientId,
                                String diagnosis, String medicine, String description,
                                String treatmentNotes, String followUpNote, String nextAppointment) {

        // Check if a record already exists for this appointment
        String checkQuery = "SELECT id FROM medical_record WHERE appointment_id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement check = conn.prepareStatement(checkQuery)) {

            check.setInt(1, appointmentId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // Record exists → UPDATE
                String updateQuery = """
                    UPDATE medical_record
                    SET diagnosis=?, medicine=?, description=?, treatment_notes=?, follow_up_note=?, next_appointment=?
                    WHERE appointment_id=?
                """;
                try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
                    ps.setString(1, diagnosis);
                    ps.setString(2, medicine);
                    ps.setString(3, description);
                    ps.setString(4, treatmentNotes);
                    ps.setString(5, followUpNote);
                    setDateOrNull(ps, 6, nextAppointment);
                    ps.setInt(7, appointmentId);
                    ps.executeUpdate();
                }
            } else {
                // No record yet → INSERT
                String insertQuery = """
                    INSERT INTO medical_record
                    (appointment_id, doctor_id, patient_id, diagnosis, medicine,
                     description, treatment_notes, follow_up_note, next_appointment)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                    ps.setInt(1, appointmentId);
                    ps.setString(2, doctorId);
                    ps.setString(3, patientId);
                    ps.setString(4, diagnosis);
                    ps.setString(5, medicine);
                    ps.setString(6, description);
                    ps.setString(7, treatmentNotes);
                    ps.setString(8, followUpNote);
                    setDateOrNull(ps, 9, nextAppointment);
                    ps.executeUpdate();
                }
            }
            return true;

        } catch (SQLException e) {
            System.out.println("MedicalRecordDaoImpl.saveOrUpdate() error: " + e.getMessage());
            return false;
        }
    }

    // ── PRIVATE HELPERS ───────────────────────────────────────────────────────

    private MedicalRecord mapRecord(ResultSet rs) throws SQLException {
        String nextAppt = rs.getString("next_appointment");
        return new MedicalRecord(
            rs.getInt("id"),
            rs.getInt("appointment_id"),
            rs.getString("doctor_id"),
            rs.getString("patient_id"),
            rs.getString("diagnosis"),
            rs.getString("medicine"),
            rs.getString("description"),
            rs.getString("treatment_notes"),
            rs.getString("follow_up_note"),
            nextAppt == null ? "" : nextAppt
        );
    }

    /**
     * Sets a DATE value on a PreparedStatement.
     * If the value is empty or null, sets SQL NULL instead.
     */
    private void setDateOrNull(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, Date.valueOf(value.trim()));
        }
    }
}
