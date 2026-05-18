package org.example.hospitalmanagementsystem.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseInitializer.java
 *
 * Creates all 5 tables when the app starts (only if they don't exist yet).
 * Also inserts a default admin account: username=admin, password=admin123
 *
 * Called once from MainApplication.java at startup.
 */
public class DatabaseInitializer {

    public static void createTables() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. admin table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS admin (
                    id       SERIAL PRIMARY KEY,
                    username VARCHAR(100) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL
                )
            """);

            // 2. doctor table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS doctor (
                    id             SERIAL PRIMARY KEY,
                    doctor_id      VARCHAR(50)  UNIQUE NOT NULL,
                    name           VARCHAR(150) NOT NULL,
                    phone_number   VARCHAR(20),
                    specialization VARCHAR(100),
                    location       VARCHAR(150),
                    age            INT,
                    username       VARCHAR(100) UNIQUE NOT NULL,
                    password       VARCHAR(100) NOT NULL
                )
            """);

            // 3. patient table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS patient (
                    id         SERIAL PRIMARY KEY,
                    patient_id VARCHAR(50)  UNIQUE NOT NULL,
                    name       VARCHAR(150) NOT NULL,
                    location   VARCHAR(150),
                    username   VARCHAR(100) UNIQUE NOT NULL,
                    password   VARCHAR(100) NOT NULL
                )
            """);

            // 4. appointment table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS appointment (
                    id               SERIAL PRIMARY KEY,
                    doctor_id        VARCHAR(50) NOT NULL REFERENCES doctor(doctor_id),
                    patient_id       VARCHAR(50) NOT NULL REFERENCES patient(patient_id),
                    appointment_date DATE NOT NULL,
                    appointment_time TIME NOT NULL,
                    status           VARCHAR(30) DEFAULT 'Scheduled',
                    UNIQUE (doctor_id, appointment_date, appointment_time)
                )
            """);

            // 5. medical_record table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS medical_record (
                    id              SERIAL PRIMARY KEY,
                    appointment_id  INT  REFERENCES appointment(id),
                    doctor_id       VARCHAR(50) REFERENCES doctor(doctor_id),
                    patient_id      VARCHAR(50) REFERENCES patient(patient_id),
                    diagnosis       TEXT,
                    medicine        TEXT,
                    description     TEXT,
                    treatment_notes TEXT,
                    follow_up_note  TEXT,
                    next_appointment DATE
                )
            """);

            System.out.println("All tables created successfully.");
            insertDefaultAdmin(conn);

        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertDefaultAdmin(Connection conn) throws SQLException {
        String check = "SELECT COUNT(*) FROM admin";
        try (PreparedStatement ps = conn.prepareStatement(check);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            if (rs.getInt(1) == 0) {
                String insert = "INSERT INTO admin (username, password) VALUES (?, ?)";
                try (PreparedStatement ins = conn.prepareStatement(insert)) {
                    ins.setString(1, "admin");
                    ins.setString(2, "admin123");
                    ins.executeUpdate();
                    System.out.println("Default admin created: username=admin | password=admin123");
                }
            }
        }
    }
}
