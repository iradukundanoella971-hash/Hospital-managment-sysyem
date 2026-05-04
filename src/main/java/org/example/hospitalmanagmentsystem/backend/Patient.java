package org.example.hospitalmanagmentsystem.backend;

import java.util.ArrayList;
import java.util.List;

public class Patient extends Person {
    private String id;
    private String location;
    private String phone;
    private String sex;
    private List<MedicalRecord> records = new ArrayList<>();

    public Patient(String id, String name, int age, String location, String phone, String sex) {
        super(name, age);
        this.id = id;
        this.location = location;
        this.phone = phone;
        this.sex = sex;
    }

    public String getId() { return id; }
    public String getLocation() { return location; }
    public String getPhone() { return phone; }
    public String getSex() { return sex; }
    public List<MedicalRecord> getRecords() { return records; }

    public void addRecord(MedicalRecord record) {
        records.add(record);
    }

    public void viewRecords() {
        if (records.isEmpty()) {
            System.out.println("No records available");
        } else {
            for (MedicalRecord r : records) {
                r.showRecord();
                System.out.println("-------------");
            }
        }
    }

    @Override
    void displayInfo() {
        System.out.println("Patient: " + getName() +
                ", Age: " + getAge() +
                ", Location: " + location +
                ", Phone: " + phone +
                ", Sex: " + sex);
    }
}