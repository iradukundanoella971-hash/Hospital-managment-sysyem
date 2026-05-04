package org.example.hospitalmanagmentsystem.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.hospitalmanagmentsystem.component.DataLoader;
import org.example.hospitalmanagmentsystem.component.HospitalContext;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        HospitalContext context = HospitalContext.getInstance();
        context.getPersistenceService().load(context.getHospital(), context.getAuthService());
        if (context.getHospital().getDoctors().isEmpty() && context.getHospital().getPatients().isEmpty()) {
            DataLoader.loadSampleData(context.getHospital(), context.getAuthService());
            context.getPersistenceService().save(context.getHospital(), context.getAuthService());
        }

        SceneNavigator.init(primaryStage);
        SceneNavigator.navigate("/fxml/Login.fxml", "Hospital Management System");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.setOnCloseRequest(event ->
                context.getPersistenceService().save(context.getHospital(), context.getAuthService()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
