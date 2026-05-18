module org.example.hospitalmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.hospitalmanagementsystem.launcher    to javafx.graphics;
    opens org.example.hospitalmanagementsystem.application to javafx.graphics;
    opens org.example.hospitalmanagementsystem.controller  to javafx.fxml;
    opens org.example.hospitalmanagementsystem.backend     to javafx.base;
}
