package org.example.hospitalmanagementsystem.database;

import org.example.hospitalmanagementsystem.backend.Appointment;
import org.example.hospitalmanagementsystem.backend.Doctor;
import org.example.hospitalmanagementsystem.backend.MedicalRecord;
import org.example.hospitalmanagementsystem.backend.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalDatabase {
    public static boolean loginAdmin(String username, String password) throws SQLException {
        String query = "SELECT id FROM admin WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true = found a matching row
        }
    }

    public static boolean loginDoctor(String username, String password) throws SQLException {
        String query = "SELECT id FROM doctor WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
    public static List<Doctor> getAllDoctors(String searchText) throws SQLException {
        List<Doctor> list = new ArrayList<>();

        String query = searchText.isEmpty()
            ? "SELECT * FROM doctor ORDER BY name"
            : "SELECT * FROM doctor WHERE name ILIKE ? OR doctor_id ILIKE ? ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            if (!searchText.isEmpty()) {
                String pattern = "%" + searchText + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapDoctor(rs));
            }
        }
        return list;
    }


    public static String getDoctorIdByUsername(String username) throws SQLException {
        String query = "SELECT doctor_id FROM doctor WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("doctor_id");
        }
        return null;
    }

    public static void insertDoctor(Doctor d) throws SQLException {
        String query = """
            INSERT INTO doctor (doctor_id, name, phone_number, specialization, location, age, username, password)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, d.getDoctorId());
            ps.setString(2, d.getName());
            ps.setString(3, d.getPhoneNumber());
            ps.setString(4, d.getSpecialization());
            ps.setString(5, d.getLocation());
            ps.setInt(6, d.getAge());
            ps.setString(7, d.getUsername());
            ps.setString(8, d.getPassword());
            ps.executeUpdate();
        }
    }

    public static void updateDoctor(Doctor d) throws SQLException {
        String query = """
            UPDATE doctor
            SET name=?, phone_number=?, specialization=?, location=?, age=?, username=?, password=?
            WHERE doctor_id=?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getPhoneNumber());
            ps.setString(3, d.getSpecialization());
            ps.setString(4, d.getLocation());
            ps.setInt(5, d.getAge());
            ps.setString(6, d.getUsername());
            ps.setString(7, d.getPassword());
            ps.setString(8, d.getDoctorId());
            ps.executeUpdate();
        }
    }
    public static void deleteDoctor(String doctorId) throws SQLException {
        String query = "DELETE FROM doctor WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, doctorId);
            ps.executeUpdate();
        }
    }

    public static boolean loginPatient(String username, String password) throws SQLException {
        String query = "SELECT id FROM patient WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
    public static List<Patient> getAllPatients() throws SQLException {
        List<Patient> list = new ArrayList<>();
        String query = "SELECT * FROM patient ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapPatient(rs));
            }
        }
        return list;
    }
    public static String getPatientIdByUsername(String username) throws SQLException {
        String query = "SELECT patient_id FROM patient WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("patient_id");
        }
        return null;
    }
    public static void insertPatient(Patient p) throws SQLException {
        String query = """
            INSERT INTO patient (patient_id, name, location, username, password)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, p.getPatientId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getLocation());
            ps.setString(4, p.getUsername());
            ps.setString(5, p.getPassword());
            ps.executeUpdate();
        }
    }
    public static List<Patient> getPatientsByDoctor(String doctorId) throws SQLException {
        List<Patient> list = new ArrayList<>();
        String query = """
            SELECT DISTINCT p.patient_id, p.name, p.location, p.username, p.password
            FROM patient p
            JOIN appointment a ON p.patient_id = a.patient_id
            WHERE a.doctor_id = ?
            ORDER BY p.name
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPatient(rs));
            }
        }
        return list;
    }

    public static List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointment ORDER BY appointment_date, appointment_time";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        }
        return list;
    }
    public static List<Appointment> getAppointmentsByDoctor(String doctorId) throws SQLException {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointment WHERE doctor_id = ? ORDER BY appointment_date, appointment_time";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        }
        return list;
    }
    public static List<Appointment> getAppointmentsByPatient(String patientId) throws SQLException {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT * FROM appointment WHERE patient_id = ? ORDER BY appointment_date, appointment_time";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        }
        return list;
    }
    public static List<String> getAppointmentIdsByDoctor(String doctorId) throws SQLException {
        List<String> ids = new ArrayList<>();
        String query = "SELECT id FROM appointment WHERE doctor_id = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(String.valueOf(rs.getInt("id")));
            }
        }
        return ids;
    }
    public static List<String> getAllDoctorIds() throws SQLException {
        List<String> ids = new ArrayList<>();
        String query = "SELECT doctor_id FROM doctor ORDER BY doctor_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getString("doctor_id"));
            }
        }
        return ids;
    }
    public static boolean insertAppointment(String doctorId, String patientId,
                                            String date, String time) throws SQLException {
        // Check for duplicate
        String dupCheck = """
            SELECT id FROM appointment
            WHERE doctor_id=? AND patient_id=? AND appointment_date=? AND appointment_time=?
        """;
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(dupCheck)) {
                ps.setString(1, doctorId);
                ps.setString(2, patientId);
                ps.setDate(3, Date.valueOf(date));
                ps.setTime(4, Time.valueOf(time + ":00"));
                if (ps.executeQuery().next()) {
                    return false; // duplicate — already booked
                }
            }


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
                return true; // booked successfully
            }
        }
    }


     //Returns the patient_id linked to a given appointment id.

    public static String getPatientIdByAppointment(int appointmentId) throws SQLException {
        String query = "SELECT patient_id FROM appointment WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("patient_id");
        }
        return null;
    }
    // 5. MEDICAL RECORD
    //Returns all medical records written by a specific doctor.

    public static List<MedicalRecord> getRecordsByDoctor(String doctorId) throws SQLException {
        List<MedicalRecord> list = new ArrayList<>();
        String query = "SELECT * FROM medical_record WHERE doctor_id = ? ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMedicalRecord(rs));
            }
        }
        return list;
    }

    //Returns all medical records for a specific patient (their history).

    public static List<MedicalRecord> getRecordsByPatient(String patientId) throws SQLException {
        List<MedicalRecord> list = new ArrayList<>();
        String query = "SELECT * FROM medical_record WHERE patient_id = ? ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMedicalRecord(rs));
            }
        }
        return list;
    }
     // Saves a medical record — inserts if new, updates if already exists for this appointment.

    public static void saveRecord(int appointmentId, String doctorId, String patientId,
                                  String diagnosis, String medicine, String description,
                                  String treatmentNotes, String followUpNote,
                                  String nextAppointment) throws SQLException {

        // Check if a record already exists for this appointment
        String checkQuery = "SELECT id FROM medical_record WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement check = conn.prepareStatement(checkQuery)) {
            check.setInt(1, appointmentId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // UPDATE existing record
                String update = """
                    UPDATE medical_record
                    SET diagnosis=?, medicine=?, description=?, treatment_notes=?, follow_up_note=?, next_appointment=?
                    WHERE appointment_id=?
                """;
                try (PreparedStatement ps = conn.prepareStatement(update)) {
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
                // INSERT new record
                String insert = """
                    INSERT INTO medical_record
                    (appointment_id, doctor_id, patient_id, diagnosis, medicine, description, treatment_notes, follow_up_note, next_appointment)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(insert)) {
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
        }
    }

    // 6. DASHBOARD COUNTS

     //Used by Admin dashboard to show total doctors, patients, appointments.

    public static int getCount(String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }
     //Returns the count of distinct patients who have appointments with a doctor.

    public static int getPatientCountForDoctor(String doctorId) {
        String query = "SELECT COUNT(DISTINCT patient_id) FROM appointment WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }
     // Returns the count of appointments for a doctor.

    public static int getAppointmentCountForDoctor(String doctorId) {
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }
     // Returns the count of appointments for a patient.

    public static int getAppointmentCountForPatient(String patientId) {
        String query = "SELECT COUNT(*) FROM appointment WHERE patient_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }
     // Returns the count of medical records for a patient.

    public static int getRecordCountForPatient(String patientId) {
        String query = "SELECT COUNT(*) FROM medical_record WHERE patient_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }
    //Converts a ResultSet row into a Doctor object.

    private static Doctor mapDoctor(ResultSet rs) throws SQLException {
        return new Doctor(
            rs.getString("doctor_id"),
            rs.getString("name"),
            rs.getString("phone_number"),
            rs.getString("specialization"),
            rs.getString("location"),
            rs.getInt("age"),
            rs.getString("username"),
            rs.getString("password")
        );
    }
     // Converts a ResultSet row into a Patient object.

    private static Patient mapPatient(ResultSet rs) throws SQLException {
        return new Patient(
            rs.getString("patient_id"),
            rs.getString("name"),
            rs.getString("location"),
            rs.getString("username"),
            rs.getString("password")
        );
    }
     // Converts a ResultSet row into an Appointment object.

    private static Appointment mapAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
            rs.getInt("id"),
            rs.getString("doctor_id"),
            rs.getString("patient_id"),
            rs.getString("appointment_date"),
            rs.getString("appointment_time"),
            rs.getString("status")
        );
    }
     //Converts a ResultSet row into a MedicalRecord object.
    private static MedicalRecord mapMedicalRecord(ResultSet rs) throws SQLException {
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

    private static void setDateOrNull(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, Date.valueOf(value.trim()));
        }
    }
}
