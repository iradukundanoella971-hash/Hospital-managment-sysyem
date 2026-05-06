package org.example.hospitalmanagmentsystem.component;

import org.example.hospitalmanagmentsystem.backend.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        validateFuture(date, slot);
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

    public void cancelAppointment(String appointmentId) {
        Appointment appointment = context.getHospital().getAppointments().get(appointmentId);
        if (appointment == null) {
            throw new InvalidDataException("Appointment not found");
        }
        appointment.setStatus("CANCELLED");
    }

    public Appointment rescheduleAppointment(String appointmentId, LocalDate newDate, LocalTime newTime) {
        Hospital hospital = context.getHospital();
        Appointment appointment = hospital.getAppointments().get(appointmentId);
        if (appointment == null) {
            throw new InvalidDataException("Appointment not found");
        }
        validateFuture(newDate, newTime);
        if (!DEFAULT_SLOTS.contains(newTime)) {
            throw new InvalidDataException("Time slot is not part of doctor availability");
        }

        String doctorId = appointment.getDoctor().getDoctorId();
        boolean occupied = hospital.getAppointments().values().stream().anyMatch(a ->
                !a.getAppointmentId().equals(appointmentId)
                        && a.getDoctor().getDoctorId().equals(doctorId)
                        && newDate.equals(a.getDate())
                        && newTime.equals(a.getTimeValue())
                        && !"CANCELLED".equalsIgnoreCase(a.getStatus()));
        if (occupied) {
            throw new InvalidDataException("Doctor already booked for selected slot");
        }
        appointment.reschedule(newDate, newTime);
        return appointment;
    }

    public List<Appointment> getAllAppointmentsSorted() {
        return context.getHospital().getAppointments().values().stream()
                .sorted(Comparator.comparing(Appointment::getDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Appointment::getTimeValue, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    private void validateFuture(LocalDate date, LocalTime slot) {
        if (LocalDateTime.of(date, slot).isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Cannot book appointment in the past");
        }
    }
}
