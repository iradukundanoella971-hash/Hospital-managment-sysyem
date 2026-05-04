package org.example.hospitalmanagmentsystem.component;

import org.example.hospitalmanagmentsystem.backend.Hospital;

public final class HospitalContext {
    private static final HospitalContext INSTANCE = new HospitalContext();

    private final Hospital hospital = new Hospital();
    private final IdGenerator idGenerator = new IdGenerator();
    private final AuthService authService = new AuthService();
    private final PersistenceService persistenceService = new PersistenceService();

    private HospitalContext() {
    }

    public static HospitalContext getInstance() {
        return INSTANCE;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }
}
