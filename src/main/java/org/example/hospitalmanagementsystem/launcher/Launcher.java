package org.example.hospitalmanagementsystem.launcher;

import org.example.hospitalmanagementsystem.application.MainApplication;


 // True entry point. We need this plain class because Maven cannot
  //run a class that directly extends Application as the main class.

public class Launcher {
    public static void main(String[] args) {
        MainApplication.main(args);
    }
}
