package org.example.hospitalmanagmentsystem.component;

import org.example.hospitalmanagmentsystem.backend.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class AppointmentService {
    private static final List<LocalTime> DEFAULT_SLOTS = Arrays.asList(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            LocalTime.of(16, 0)
    );

    private final HospitalContext context = HospitalContext.getInstance();

    public List<LocalTime> getDefaultSlots() {
        return DEFAULT_SLOTS;
    }

    public Appointment bookAppointment(String patientId, String doctorId, LocalDate date, LocalTime slot) {
        Hospital hospital = context.getHospital();
        Patient patient = hospital.getPatients().get(patientId);
        Doctor doctor = hospital.getDoctors().get(doctorId);

        if (patient == null || doctor == null) {
            throw new InvalidDataException("Doctor or patient not found");
        }
        if (date == null || slot == null) {
            throw new InvalidDataException("Date and time are required");
        }
        if (LocalDateTime.of(date, slot).isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Cannot book appointment in the past");
        }
        if (!DEFAULT_SLOTS.contains(slot)) {
            throw new InvalidDataException("Time slot is not part of doctor availability");
        }

        boolean occupied = hospital.getAppointments().values().stream().anyMatch(a ->
                a.getDoctor().getDoctorId().equals(doctorId)
                        && date.equals(a.getDate())
                        && slot.equals(a.getTimeValue())
                        && !"CANCELLED".equalsIgnoreCase(a.getStatus()));
        if (occupied) {
            throw new InvalidDataException("Doctor already booked for selected slot");
        }

        String id = context.getIdGenerator().nextAppointmentId();
        Appointment appointment = new Appointment(id, doctor, patient, date, slot);
        doctor.addAppointment(appointment);
        hospital.getAppointments().put(id, appointment);
        return appointment;
    }
}
