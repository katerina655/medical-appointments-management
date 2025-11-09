package mainpackage.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an appointment between a patient and a doctor at a
 * particular date and time.  Instances of this class link the
 * relevant patient and doctor and store the appointment time.
 */
public class Appointment {
    /** The patient involved in the appointment. */
    private final Patient patient;
    /** The doctor involved in the appointment. */
    private final Doctor doctor;
    /** The date and time of the appointment. */
    private final LocalDateTime dateTime;

    /**
     * Creates a new appointment with the given patient, doctor and
     * scheduled time.
     *
     * @param patient  the patient attending
     * @param doctor   the doctor seeing the patient
     * @param dateTime the date and time of the appointment
     */
    public Appointment(Patient patient, Doctor doctor, LocalDateTime dateTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
    }

    /** Returns the patient associated with this appointment. */
    public Patient getPatient() {
        return patient;
    }

    /** Returns the doctor associated with this appointment. */
    public Doctor getDoctor() {
        return doctor;
    }

    /** Returns the date and time of the appointment. */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Appointment{" +
                "patient=" + patient.getName() + " " + patient.getSurname() +
                ", doctor=" + doctor.getName() + " " + doctor.getSurname() +
                ", dateTime=" + dateTime.format(formatter) +
                '}';
    }
}