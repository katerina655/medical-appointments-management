package mainpackage.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the appointment management system.
 * <p>
 * Patients are also users of the system and therefore inherit the
 * login/logout capabilities from {@link Users}. Each patient has:
 * - a unique AMKA (Greek social security number) that cannot change,
 * - a mandatory date of birth,
 * - a mandatory insurance ID.
 * Patients can register themselves, search for available appointments
 * and maintain lists of scheduled and past appointments.
 */
public class Patient extends Users {

    /** The unique AMKA identifier for this patient (immutable). */
    private final String amka;

    /** Mandatory date of birth. */
    private final LocalDate dateOfBirth;

    /** Mandatory insurance identifier. */
    private final String insuranceId;

    /** A list of upcoming appointments scheduled for the patient. */
    private final List<Appointment> scheduledAppointments;

    /** A list containing past (completed) appointments. */
    private final List<Appointment> appointmentHistory;

    /**
     * Constructs a new patient with the supplied details.
     *
     * @param username     the login username
     * @param password     the login password
     * @param name         the patient's first name
     * @param surname      the patient's last name
     * @param gender       the patient's gender (required)
     * @param amka         the AMKA number (exactly 11 digits)
     * @param dateOfBirth  the patient's date of birth (required)
     * @param insuranceId  the patient's insurance id (required, non-empty)
     */
    public Patient(String username,
                   String password,
                   String name,
                   String surname,
                   Users.Gender gender,
                   String amka,
                   LocalDate dateOfBirth,
                   String insuranceId) {

        super(username, password, name, surname, gender);

        if (amka == null || !amka.matches("\\d{11}")) {
            throw new IllegalArgumentException("AMKA must be exactly 11 digits.");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth is required.");
        }
        if (insuranceId == null || insuranceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Insurance ID is required.");
        }

        this.amka = amka;
        this.dateOfBirth = dateOfBirth;
        this.insuranceId = insuranceId.trim();

        this.scheduledAppointments = new ArrayList<>();
        this.appointmentHistory = new ArrayList<>();
    }

    // -------- Getters --------
    public String getAmka() { return amka; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getInsuranceId() { return insuranceId; }

    /** Convenience: current age in full years. */
    public int getAge() {
        return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // -------- Behaviour (όπως τα είχες) --------

    /** Registers a new patient (demo output). */
    public void registration() {
        System.out.println("Patient " + name + " " + surname + " registered successfully.");
    }

    /** Adds an appointment to the list of scheduled appointments. */
    public void addScheduledAppointment(Appointment appointment) {
        scheduledAppointments.add(appointment);
    }

    /** Archives an appointment from scheduled to history. */
    public void archiveAppointment(Appointment appointment) {
        if (scheduledAppointments.remove(appointment)) {
            appointmentHistory.add(appointment);
        }
    }

    /** Returns a live list of scheduled appointments. */
    public List<Appointment> getScheduledAppointments() {
        return scheduledAppointments;
    }

    /** Returns a live list of past appointments. */
    public List<Appointment> getAppointmentHistory() {
        return appointmentHistory;
    }

    /**
     * Attempts to book an appointment with a specific doctor at a given time.
     * If the doctor is available it creates and returns an Appointment; otherwise null.
     */
    public Appointment searchAndBookAppointment(Doctor doctor, LocalDateTime dateTime) {
        if (doctor == null || dateTime == null) return null;
        if (doctor.isAvailable(dateTime)) {
            Appointment ap = new Appointment(this, doctor, dateTime);
            this.addScheduledAppointment(ap);
            doctor.bookAppointment(ap); // διατηρώ τη δική σου ροή
            return ap;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender +
                ", amka='" + amka + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", insuranceId='" + insuranceId + '\'' +
                '}';
    }
}
