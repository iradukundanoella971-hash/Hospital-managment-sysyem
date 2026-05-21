package org.example.hospitalmanagementsystem.database.dao;

import java.util.List;

public interface GenericDao<T, ID> {

    boolean save(T object);

    boolean update(T object);

    boolean delete(ID id);

    T get(ID id);

    List<T> getAll();
}
