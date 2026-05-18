package org.example.hospitalmanagementsystem.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hospitalmanagementsystem.database.DatabaseInitializer;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        DatabaseInitializer.createTables();
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/org/example/hospitalmanagementsystem/fxml/home.fxml"
            )
        );

        Scene scene = new Scene(loader.load(), 1100, 700);
        stage.setTitle("Hospital Management System");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
