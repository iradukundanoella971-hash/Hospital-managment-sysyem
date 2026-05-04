package org.example.hospitalmanagmentsystem.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneNavigator {
    private static Stage stage;

    private SceneNavigator() {
    }

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void navigate(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 760);
            scene.getStylesheets().add(SceneNavigator.class.getResource("/styles/app.css").toExternalForm());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to navigate to " + fxmlPath, e);
        }
    }
}
