package mainpackage.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a doctor in the appointment management system.
 * Doctors are users who possess a medical specialty and can declare
 * availability for appointments. Each doctor also has a license number
 * and years of experience.
 */
public class Doctor extends Users {
    /** The medical specialty of this doctor (e.g. cardiologist). */
    private String specialty;

    /** The doctor's license number (required). */
    private final String licenseNumber;

    /** Years of experience (must be >= 0). */
    private final int yearsExperience;

    /** A set of available appointment slots (no duplicates). */
    private final Set<LocalDateTime> availableSlots;

    /** A list of appointments that have been booked with this doctor. */
    private final List<Appointment> appointments;

    /**
     * Constructs a doctor with the specified details.
     *
     * @param username       the login username
     * @param password       the login password
     * @param name           the first name
     * @param surname        the last name
     * @param gender         the gender
     * @param specialty      the doctor's medical specialty
     * @param licenseNumber  the license number (required, non-empty)
     * @param yearsExperience the years of experience (>= 0)
     */
    public Doctor(String username, String password, String name, String surname,
                  Users.Gender gender, String specialty,
                  String licenseNumber, int yearsExperience) {
        super(username, password, name, surname, gender);

        if (specialty == null || specialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialty is required.");
        }
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("License number is required.");
        }
        if (yearsExperience < 0) {
            throw new IllegalArgumentException("Years of experience cannot be negative.");
        }

        this.specialty = specialty.trim();
        this.licenseNumber = licenseNumber.trim();
        this.yearsExperience = yearsExperience;
        this.availableSlots = new HashSet<>();
        this.appointments = new ArrayList<>();
    }

    // -------- Getters/Setters --------
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLicenseNumber() { return licenseNumber; }
    public int getYearsExperience() { return yearsExperience; }

    // -------- Availability methods --------
    public boolean addAvailability(LocalDateTime slot) { return availableSlots.add(slot); }

    public boolean isAvailable(LocalDateTime dateTime) {
        if (!availableSlots.contains(dateTime)) return false;
        for (Appointment ap : appointments) {
            if (ap.getDateTime().equals(dateTime)) return false;
        }
        return true;
    }

    public void bookAppointment(Appointment appointment) {
        if (appointment != null) {
            appointments.add(appointment);
        }
    }

    public List<Appointment> getAppointments() { return appointments; }
    public Set<LocalDateTime> getAvailableSlots() { return availableSlots; }

    public void viewSchedule() {
        System.out.println("Schedule for Dr. " + name + " " + surname + ":");
        if (appointments.isEmpty()) {
            System.out.println("  No appointments scheduled.");
        } else {
            for (Appointment ap : appointments) {
                System.out.println("  " + ap);
            }
        }
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender +
                ", specialty='" + specialty + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", yearsExperience=" + yearsExperience +
                '}';
    }
}
