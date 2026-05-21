package org.example.hospitalmanagementsystem.database.dao;

import org.example.hospitalmanagementsystem.backend.Appointment;
import org.example.hospitalmanagementsystem.database.dao.GenericDao;

import java.util.List;
public interface AppointmentDao extends GenericDao<Appointment, Integer> {
    List<Appointment> getByDoctor(String doctorId);

    List<Appointment> getByPatient(String patientId);

    List<String> getAppointmentIdsByDoctor(String doctorId);

    List<String> getAllDoctorIds();

    String getPatientIdByAppointment(int appointmentId);

    boolean book(String doctorId, String patientId, String date, String time);
}
