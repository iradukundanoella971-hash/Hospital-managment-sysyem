package org.example.hospitalmanagementsystem.database.dao;

import org.example.hospitalmanagementsystem.backend.Patient;
import org.example.hospitalmanagementsystem.database.dao.GenericDao;

import java.util.List;

public interface PatientDao extends GenericDao<Patient, String> {


    boolean login(String username, String password);

    String getPatientIdByUsername(String username);

    List<Patient> getPatientsByDoctor(String doctorId);
}
