package org.example.hospitalmanagmentsystem.backend;

public abstract class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Name cannot be empty");
        }
        if (age <= 0) {
            throw new InvalidDataException("Age must be valid");
        }
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    public abstract void displayInfo();
}