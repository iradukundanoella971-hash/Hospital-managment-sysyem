package org.example.hospitalmanagementsystem.database.dao;

import org.example.hospitalmanagementsystem.backend.MedicalRecord;
import org.example.hospitalmanagementsystem.database.dao.GenericDao;

import java.util.List;


public interface MedicalRecordDao extends GenericDao<MedicalRecord, Integer> {

    List<MedicalRecord> getByDoctor(String doctorId);

    List<MedicalRecord> getByPatient(String patientId);


    boolean saveOrUpdate(int appointmentId, String doctorId, String patientId,
                         String diagnosis, String medicine, String description,
                         String treatmentNotes, String followUpNote, String nextAppointment);
}
