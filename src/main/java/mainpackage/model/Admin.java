package mainpackage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an administrative user in the appointment management
 * application. Administrators can add doctors to the system and
 * maintain a list of all registered doctors. They inherit basic
 * user behaviour such as login/logout from {@link Users}.
 */
public class Admin extends Users {

    /** List of doctors managed by this administrator. */
    private final List<Doctor> doctors;

    /** Office/location of the administrator (required). */
    private final String office;

    /** Whether this admin can approve doctors (required flag). */
    private final boolean canApproveDoctors;

    /**
     * Constructs a new administrator.
     *
     * @param username           the login username
     * @param password           the login password
     * @param name               the administrator's first name
     * @param surname            the administrator's last name
     * @param gender             the administrator's gender (required)
     * @param office             office/location (required, non-empty)
     * @param canApproveDoctors  true if admin has approval rights
     */
    public Admin(String username,
                 String password,
                 String name,
                 String surname,
                 Users.Gender gender,
                 String office,
                 boolean canApproveDoctors) {
        super(username, password, name, surname, gender);

        if (office == null || office.trim().isEmpty()) {
            throw new IllegalArgumentException("Office is required.");
        }
        this.office = office.trim();
        this.canApproveDoctors = canApproveDoctors;

        this.doctors = new ArrayList<>();
    }

    /** Adds a doctor to the administrator's list. */
    public void addDoctor(Doctor doctor) {
        if (doctor != null) {
            doctors.add(doctor);
            System.out.println("Administrator " + name + " added doctor "
                    + doctor.getName() + " " + doctor.getSurname() + ".");
        }
    }

    /** Approves a doctor if this admin has approval rights. */
    public void approveDoctor(Doctor doctor) {
        if (!canApproveDoctors || doctor == null) return;
        System.out.println("Administrator " + name + " approved doctor " + doctor.getUsername());
    }

    /** Returns an immutable snapshot of the doctor's list. */
    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors);
    }

    /** Prints all doctors managed by this administrator. */
    public void listDoctors() {
        System.out.println("Doctors managed by administrator " + name + ":");
        if (doctors.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (Doctor d : doctors) {
                System.out.println("  " + d);
            }
        }
    }

    // Getters for the new fields
    public String getOffice() { return office; }
    public boolean canApproveDoctors() { return canApproveDoctors; }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender +
                ", office='" + office + '\'' +
                ", canApproveDoctors=" + canApproveDoctors +
                '}';
    }
}
