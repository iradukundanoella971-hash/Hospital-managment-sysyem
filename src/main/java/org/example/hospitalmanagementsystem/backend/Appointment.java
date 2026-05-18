package org.example.hospitalmanagementsystem.backend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Appointment {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty  doctorId;
    private final SimpleStringProperty  patientId;
    private final SimpleStringProperty  date;
    private final SimpleStringProperty  time;
    private final SimpleStringProperty  status;

    public Appointment(int id, String doctorId, String patientId,
                       String date, String time, String status) {
        this.id        = new SimpleIntegerProperty(id);
        this.doctorId  = new SimpleStringProperty(doctorId);
        this.patientId = new SimpleStringProperty(patientId);
        this.date      = new SimpleStringProperty(date);
        this.time      = new SimpleStringProperty(time);
        this.status    = new SimpleStringProperty(status);
    }

    public int    getId()        { return id.get(); }
    public String getDoctorId()  { return doctorId.get(); }
    public String getPatientId() { return patientId.get(); }
    public String getDate()      { return date.get(); }
    public String getTime()      { return time.get(); }
    public String getStatus()    { return status.get(); }

    public SimpleIntegerProperty idProperty()        { return id; }
    public SimpleStringProperty  doctorIdProperty()  { return doctorId; }
    public SimpleStringProperty  patientIdProperty() { return patientId; }
    public SimpleStringProperty  dateProperty()      { return date; }
    public SimpleStringProperty  timeProperty()      { return time; }
    public SimpleStringProperty  statusProperty()    { return status; }
}
