package org.example.hospitalmanagementsystem.backend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MedicalRecord {

    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty appointmentId;
    private final SimpleStringProperty  doctorId;
    private final SimpleStringProperty  patientId;
    private final SimpleStringProperty  diagnosis;
    private final SimpleStringProperty  medicine;
    private final SimpleStringProperty  description;
    private final SimpleStringProperty  treatmentNotes;
    private final SimpleStringProperty  followUpNote;
    private final SimpleStringProperty  nextAppointment;

    public MedicalRecord(int id, int appointmentId, String doctorId, String patientId,
                         String diagnosis, String medicine, String description,
                         String treatmentNotes, String followUpNote, String nextAppointment) {
        this.id              = new SimpleIntegerProperty(id);
        this.appointmentId   = new SimpleIntegerProperty(appointmentId);
        this.doctorId        = new SimpleStringProperty(doctorId);
        this.patientId       = new SimpleStringProperty(patientId);
        this.diagnosis       = new SimpleStringProperty(diagnosis);
        this.medicine        = new SimpleStringProperty(medicine);
        this.description     = new SimpleStringProperty(description);
        this.treatmentNotes  = new SimpleStringProperty(treatmentNotes);
        this.followUpNote    = new SimpleStringProperty(followUpNote);
        this.nextAppointment = new SimpleStringProperty(nextAppointment);
    }

    public int    getId()              { return id.get(); }
    public int    getAppointmentId()   { return appointmentId.get(); }
    public String getDoctorId()        { return doctorId.get(); }
    public String getPatientId()       { return patientId.get(); }
    public String getDiagnosis()       { return diagnosis.get(); }
    public String getMedicine()        { return medicine.get(); }
    public String getDescription()     { return description.get(); }
    public String getTreatmentNotes()  { return treatmentNotes.get(); }
    public String getFollowUpNote()    { return followUpNote.get(); }
    public String getNextAppointment() { return nextAppointment.get(); }

    public SimpleIntegerProperty idProperty()              { return id; }
    public SimpleIntegerProperty appointmentIdProperty()   { return appointmentId; }
    public SimpleStringProperty  doctorIdProperty()        { return doctorId; }
    public SimpleStringProperty  patientIdProperty()       { return patientId; }
    public SimpleStringProperty  diagnosisProperty()       { return diagnosis; }
    public SimpleStringProperty  medicineProperty()        { return medicine; }
    public SimpleStringProperty  descriptionProperty()     { return description; }
    public SimpleStringProperty  treatmentNotesProperty()  { return treatmentNotes; }
    public SimpleStringProperty  followUpNoteProperty()    { return followUpNote; }
    public SimpleStringProperty  nextAppointmentProperty() { return nextAppointment; }
}
