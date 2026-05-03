package org.example.hospitalmanagmentsystem.backend;

public class MedicalRecord {
    public String description;
    public String medicine;

    public MedicalRecord(String description, String medicine) {
        this.description = description;
        this.medicine = medicine;
    }

    public void showRecord() {
        System.out.println("Diagnosis: " + description);
        System.out.println("Medicine: " + medicine);
    }
}