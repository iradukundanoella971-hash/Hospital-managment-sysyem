package org.example.hospitalmanagmentsystem.component;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private final AtomicInteger doctorSeq = new AtomicInteger(100);
    private final AtomicInteger patientSeq = new AtomicInteger(1000);
    private final AtomicInteger appointmentSeq = new AtomicInteger(1);

    public String nextDoctorId() {
        return "D" + doctorSeq.getAndIncrement();
    }

    public String nextPatientId() {
        return "P" + patientSeq.getAndIncrement();
    }

    public String nextAppointmentId() {
        return "A" + String.format("%04d", appointmentSeq.getAndIncrement());
    }
}
