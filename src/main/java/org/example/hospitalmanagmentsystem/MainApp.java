package org.example.hospitalmanagmentsystem;

import org.example.hospitalmanagmentsystem.backend.*;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("=== HOSPITAL MANAGEMENT SYSTEM ===\n");

        // Create hospital
        Hospital hospital = new Hospital();

        // Create doctor
        Doctor d1 = new Doctor("D1", "Dr. John Smith", 45, "Cardiology", "1234");
        hospital.registerDoctor(d1);

        // Create patient
        Patient p1 = new Patient("P1", "Alice Johnson", 25, "Kigali", "0780000000", "Female");
        hospital.registerPatient(p1);

        // Book appointment
        hospital.bookAppointment("Cardiology", p1, "10:00 AM");

        // Login doctor
        if (d1.login("Dr. John Smith", "1234")) {
            System.out.println("\n✓ Doctor logged in successfully!");
            System.out.println("\n--- Appointments ---");
            for (Appointment a : d1.appointments) {
                a.showAppointment();
            }
        }

        // Add medical record
        MedicalRecord record = new MedicalRecord("Heart checkup", "Aspirin");
        p1.addRecord(record);

        // View records
        System.out.println("\n--- Patient Medical Records ---");
        p1.viewRecords();

        System.out.println("\n✓ System working!");
    }
}