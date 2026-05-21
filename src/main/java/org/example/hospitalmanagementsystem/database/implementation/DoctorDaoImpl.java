package org.example.hospitalmanagementsystem.database.implementation;

import org.example.hospitalmanagementsystem.backend.Doctor;
import org.example.hospitalmanagementsystem.database.Connect;
import org.example.hospitalmanagementsystem.database.dao.DoctorDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DoctorDaoImpl.java
 *
 * This class implements DoctorDao.
 * It contains all the actual SQL code for Doctor operations.
 *
 * Every method:
 *  1. Opens a connection using Connect.getConnection()
 *  2. Creates a PreparedStatement with ? placeholders
 *  3. Sets the values for each ?
 *  4. Executes the query
 *  5. Returns the result
 *  6. Closes the connection automatically (try-with-resources)
 */
public class DoctorDaoImpl implements DoctorDao {

    // ── SAVE (INSERT) ─────────────────────────────────────────────────────────

    /**
     * Inserts a new doctor into the doctor table.
     * Returns true if the insert worked, false if there was an error.
     */
    @Override
    public boolean save(Doctor doctor) {
        String query = """
            INSERT INTO doctor
            (doctor_id, name, phone_number, specialization, location, age, username, password)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctor.getDoctorId());
            ps.setString(2, doctor.getName());
            ps.setString(3, doctor.getPhoneNumber());
            ps.setString(4, doctor.getSpecialization());
            ps.setString(5, doctor.getLocation());
            ps.setInt(6, doctor.getAge());
            ps.setString(7, doctor.getUsername());
            ps.setString(8, doctor.getPassword());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("DoctorDaoImpl.save() error: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    /**
     * Updates an existing doctor row.
     * We find the row using doctor_id (the WHERE clause).
     */
    @Override
    public boolean update(Doctor doctor) {
        String query = """
            UPDATE doctor
            SET name=?, phone_number=?, specialization=?, location=?, age=?, username=?, password=?
            WHERE doctor_id=?
        """;

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getPhoneNumber());
            ps.setString(3, doctor.getSpecialization());
            ps.setString(4, doctor.getLocation());
            ps.setInt(5, doctor.getAge());
            ps.setString(6, doctor.getUsername());
            ps.setString(7, doctor.getPassword());
            ps.setString(8, doctor.getDoctorId()); // WHERE doctor_id = ?

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("DoctorDaoImpl.update() error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    /**
     * Deletes a doctor by their doctor_id.
     */
    @Override
    public boolean delete(String doctorId) {
        String query = "DELETE FROM doctor WHERE doctor_id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("DoctorDaoImpl.delete() error: " + e.getMessage());
            return false;
        }
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────

    /**
     * Returns one doctor by their doctor_id.
     * Returns null if not found.
     */
    @Override
    public Doctor get(String doctorId) {
        String query = "SELECT * FROM doctor WHERE doctor_id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapDoctor(rs); // convert the row to a Doctor object
            }

        } catch (SQLException e) {
            System.out.println("DoctorDaoImpl.get() error: " + e.getMessage());
        }
        return null; // not found
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────

    /**
     * Returns all doctors from the database, ordered by name.
     */
    @Override
    public List<Doctor> getAll() {
        List<Doctor> list = new ArrayList<>();
        String query = "SELECT * FROM doctor ORDER BY name";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapDoctor(rs));
            }

        } catch (SQLException e) {
            System.out.println("DoctorDaoImpl.getAll() error: " + e.getMessage());
        }
        return list;
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────

    /**
     * Checks if the username and password match a doctor in the database.
     * Returns true if login is valid.
     */
    @Override
    public boolean login(String username, String password) {
        String query = "SELECT id FROM doctor WHERE username = ? AND password = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true = found a matching row

        } catch (SQLException e) {
            System.out.println("DoctorDaoImpl.login() error: " + e.getMessage());
            return false;
        }
    }

    // ── GET DOCTOR ID BY USERNAME ─────────────────────────────────────────────

    /**
     * Returns the doctor_id for a given username.
     * Used after login to know which doctor is logged in.
     */
    @Override
    public String getDoctorIdByUsername(String username) {
        String query = "SELECT doctor_id FROM doctor WHERE username = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("doctor_id");

        } catch (SQLException e) {
            System.out.println("DoctorDaoImpl.getDoctorIdByUsername() error: " + e.getMessage());
        }
        return null;
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────

    /**
     * Searches doctors by name or doctor_id using ILIKE (case-insensitive).
     * If searchText is empty, returns all doctors.
     */
    @Override
    public List<Doctor> search(String searchText) {
        List<Doctor> list = new ArrayList<>();

        // If empty search → return all
        if (searchText == null || searchText.isEmpty()) {
            return getAll();
        }

        String query = "SELECT * FROM doctor WHERE name ILIKE ? OR doctor_id ILIKE ? ORDER BY name";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            String pattern = "%" + searchText + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapDoctor(rs));
            }

        } catch (SQLException e) {
            System.out.println("DoctorDaoImpl.search() error: " + e.getMessage());
        }
        return list;
    }

    // ── PRIVATE HELPER ────────────────────────────────────────────────────────

    /**
     * Converts one row from the ResultSet into a Doctor object.
     * This avoids repeating the same code in every method.
     */
    private Doctor mapDoctor(ResultSet rs) throws SQLException {
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
}
