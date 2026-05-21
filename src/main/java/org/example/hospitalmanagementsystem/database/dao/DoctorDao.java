package org.example.hospitalmanagementsystem.database.dao;

import org.example.hospitalmanagementsystem.backend.Doctor;
import org.example.hospitalmanagementsystem.database.dao.GenericDao;

import java.util.List;

public interface DoctorDao extends GenericDao<Doctor, String> {

    boolean login(String username, String password);

    String getDoctorIdByUsername(String username);
    List<Doctor> search(String searchText);
}
