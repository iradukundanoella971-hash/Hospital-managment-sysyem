module org.example.hospitalmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.hospitalmanagementsystem.launcher        to javafx.graphics;
    opens org.example.hospitalmanagementsystem.application     to javafx.graphics;
    opens org.example.hospitalmanagementsystem.controller      to javafx.fxml;
    opens org.example.hospitalmanagementsystem.backend         to javafx.base;
    opens org.example.hospitalmanagementsystem.database        to javafx.base;
    opens org.example.hospitalmanagementsystem.database.dao    to javafx.base;
    opens org.example.hospitalmanagementsystem.database.implementation to javafx.base;

    exports org.example.hospitalmanagementsystem.backend;
    exports org.example.hospitalmanagementsystem.database;
    exports org.example.hospitalmanagementsystem.database.dao;
    exports org.example.hospitalmanagementsystem.database.implementation;
    exports org.example.hospitalmanagementsystem.controller;
    exports org.example.hospitalmanagementsystem.application;
    exports org.example.hospitalmanagementsystem.launcher;
}
