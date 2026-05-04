package org.example.hospitalmanagmentsystem.component;

import org.example.hospitalmanagmentsystem.backend.*;

public class DataLoader {

    public static void loadSampleData(Hospital hospital, AuthService authService) {
        try {
            // Add sample doctors
            Doctor d1 = new Doctor("D001", "Dr. Sarah Johnson", 45, "Cardiology", "doctor123");
            Doctor d2 = new Doctor("D002", "Dr. Michael Chen", 52, "Neurology", "doctor123");
            Doctor d3 = new Doctor("D003", "Dr. Emily Rodriguez", 38, "Pediatrics", "doctor123");

            hospital.registerDoctor(d1);
            hospital.registerDoctor(d2);
            hospital.registerDoctor(d3);

            // Add sample patients
            Patient p1 = new Patient("P001", "Alice Wonderland", 28, "New York", "+1234567890", "Female");
            Patient p2 = new Patient("P002", "Bob Marley", 35, "Los Angeles", "+1234567891", "Male");
            Patient p3 = new Patient("P003", "Charlie Brown", 12, "Chicago", "+1234567892", "Male");

            hospital.registerPatient(p1);
            hospital.registerPatient(p2);
            hospital.registerPatient(p3);

            // Sample medical records
            p1.addRecord(new MedicalRecord("Annual checkup - healthy", "None"));
            p2.addRecord(new MedicalRecord("High blood pressure", "Lisinopril 10mg"));
            p3.addRecord(new MedicalRecord("Flu symptoms", "Antiviral medication"));

            authService.createAdmin("admin", "admin123");
            authService.createDoctorAccount(d1, "sarah", "doctor123");
            authService.createDoctorAccount(d2, "michael", "doctor123");
            authService.createDoctorAccount(d3, "emily", "doctor123");
            authService.createPatientAccount(p1, "alice", "patient123");
            authService.createPatientAccount(p2, "bob", "patient123");
            authService.createPatientAccount(p3, "charlie", "patient123");

        } catch (Exception e) {
            System.out.println("Sample data already exists or error: " + e.getMessage());
        }
    }
}