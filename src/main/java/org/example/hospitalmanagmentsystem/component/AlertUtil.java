package org.example.hospitalmanagmentsystem.component;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class AlertUtil {

    public static void showError(Label statusLabel, String message) {
        statusLabel.setText("❌ " + message);
        statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(e -> statusLabel.setText(""));
        pause.play();
    }

    public static void showSuccess(Label statusLabel, String message) {
        statusLabel.setText("✓ " + message);
        statusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px;");

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> statusLabel.setText(""));
        pause.play();
    }

    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}