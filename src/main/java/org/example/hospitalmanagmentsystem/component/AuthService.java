package org.example.hospitalmanagmentsystem.component;

import org.example.hospitalmanagmentsystem.backend.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, UserAccount> users = new LinkedHashMap<>();

    public Map<String, UserAccount> getUsers() {
        return users;
    }

    public UserAccount login(String username, String password) {
        UserAccount account = users.get(username);
        if (account == null || !account.isActive()) {
            throw new InvalidDataException("Invalid login credentials");
        }
        if (!account.getPasswordHash().equals(hash(password))) {
            throw new InvalidDataException("Invalid login credentials");
        }
        return account;
    }

    public UserAccount createAdmin(String username, String password) {
        return createUser(username, password, Role.ADMIN, "ADMIN");
    }

    public UserAccount createDoctorAccount(Doctor doctor, String username, String password) {
        return createUser(username, password, Role.DOCTOR, doctor.getDoctorId());
    }

    public UserAccount createPatientAccount(Patient patient, String username, String password) {
        return createUser(username, password, Role.PATIENT, patient.getId());
    }

    public void deactivate(String username) {
        UserAccount account = users.get(username);
        if (account != null) {
            account.setActive(false);
        }
    }

    private UserAccount createUser(String username, String password, Role role, String linkedId) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new InvalidDataException("Username and password are required");
        }
        if (users.containsKey(username)) {
            throw new InvalidDataException("Username already exists");
        }
        UserAccount account = new UserAccount(username, hash(password), role, linkedId, true);
        users.put(username, account);
        return account;
    }

    public static String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm unavailable", e);
        }
    }
}
