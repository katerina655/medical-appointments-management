package mainpackage.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Demo κονσόλας για το σύστημα ραντεβού:
 * - Δημιουργεί Patient, Doctor, Admin με ΟΛΑ τα νέα required πεδία
 * - Κάνει login/logout και δείχνει τον static μετρητή χρηστών
 * - Καταχωρεί διαθεσιμότητα γιατρού και επιχειρεί κράτηση ραντεβού
 * - Κάνει validation εισόδου με try/catch
 * - Διαβάζει ασθενείς από αρχείο (βήμα 6) με 8 πεδία/γραμμή
 */
public class CreateUsers {

    /** Formatter για ημερομηνίες/ώρες ραντεβού. */
    private static final DateTimeFormatter DATETIME_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Formatter για ημερομηνίες γέννησης. */
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        System.out.println("=== Σύστημα Διαχείρισης Ιατρικών Ραντεβού (Demo) ===");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        try {
            // -----------------------------
            // Δημιουργία PATIENT (με όλα τα required)
            // -----------------------------
            System.out.println("=== Create Patient ===");
            String pUsername = readNonEmpty(scanner, "Enter patient username: ");
            String pPassword = readNonEmpty(scanner, "Enter patient password: ");
            String pName     = readNonEmpty(scanner, "Enter patient first name: ");
            String pSurname  = readNonEmpty(scanner, "Enter patient last name: ");
            Users.Gender pGender = readGender(scanner, "Enter patient gender (MALE/FEMALE/OTHER): ");
            String pAmka     = readPattern(scanner, "Enter patient AMKA (11 digits): ",
                    "\\d{11}", "AMKA must consist of exactly 11 digits.");
            LocalDate pDob   = readLocalDate(scanner, "Enter patient date of birth (dd/MM/yyyy): ");
            String pInsId    = readNonEmpty(scanner, "Enter patient insurance ID: ");

            Patient patient = new Patient(
                    pUsername, pPassword, pName, pSurname,
                    pGender, pAmka, pDob, pInsId
            );

            patient.registration();
            patient.login();
            System.out.println("Users so far: " + Users.getUsersCounter());
            patient.logout();

            System.out.println();

            // -----------------------------
            // Δημιουργία DOCTOR (με όλα τα required)
            // -----------------------------
            System.out.println("=== Create Doctor ===");
            String dUsername  = readNonEmpty(scanner, "Enter doctor username: ");
            String dPassword  = readNonEmpty(scanner, "Enter doctor password: ");
            String dName      = readNonEmpty(scanner, "Enter doctor first name: ");
            String dSurname   = readNonEmpty(scanner, "Enter doctor last name: ");
            Users.Gender dGender = readGender(scanner, "Enter doctor gender (MALE/FEMALE/OTHER): ");
            String dSpecialty = readNonEmpty(scanner, "Enter doctor specialty: ");
            String dLicense   = readNonEmpty(scanner, "Enter doctor license number: ");
            int dYearsExp     = readPositiveIntAllowZero(scanner, "Enter years of experience (>=0): ");

            Doctor doctor = new Doctor(
                    dUsername, dPassword, dName, dSurname,
                    dGender, dSpecialty, dLicense, dYearsExp
            );

            doctor.login();
            System.out.println("Users so far: " + Users.getUsersCounter());
            doctor.logout();

            // -----------------------------
            // Εισαγωγή διαθεσιμότητας ιατρού
            // -----------------------------
            int slotsCount = readPositiveInt(scanner, "Enter number of available time slots to add: ");
            for (int i = 1; i <= slotsCount; i++) {
                LocalDateTime slot = readDateTime(scanner,
                        "Enter slot " + i + " (format dd/MM/yyyy HH:mm): ");
                boolean added = doctor.addAvailability(slot);
                if (added) {
                    System.out.println("  Added availability: " + slot.format(DATETIME_FMT));
                } else {
                    System.out.println("  Slot already exists (duplicate). Skipped.");
                }
            }

            System.out.println();

            // -----------------------------
            // Δημιουργία ADMIN (με όλα τα required) & καταχώρηση γιατρού
            // -----------------------------
            System.out.println("=== Create Admin ===");
            String aUsername = readNonEmpty(scanner, "Enter admin username: ");
            String aPassword = readNonEmpty(scanner, "Enter admin password: ");
            String aName     = readNonEmpty(scanner, "Enter admin first name: ");
            String aSurname  = readNonEmpty(scanner, "Enter admin last name: ");
            Users.Gender aGender = readGender(scanner, "Enter admin gender (MALE/FEMALE/OTHER): ");
            String aOffice   = readNonEmpty(scanner, "Enter admin office/location: ");
            boolean aCanApprove = readBooleanYesNo(scanner, "Can admin approve doctors? (Y/N): ");

            Admin admin = new Admin(
                    aUsername, aPassword, aName, aSurname,
                    aGender, aOffice, aCanApprove
            );

            admin.login();
            System.out.println("Users so far: " + Users.getUsersCounter());
            admin.addDoctor(doctor);
            if (aCanApprove) admin.approveDoctor(doctor);
            System.out.println("Doctor added by admin.");
            admin.logout();

            System.out.println();

            // -----------------------------
            // Απόπειρα κράτησης ραντεβού
            // -----------------------------
            System.out.println("=== Attempt to Book Appointment ===");
            LocalDateTime desired = readDateTime(scanner,
                    "Enter desired appointment date/time (dd/MM/yyyy HH:mm): ");

            System.out.println("Doctor current availability (before booking):");
            doctor.viewSchedule();

            Appointment appointment = patient.searchAndBookAppointment(doctor, desired);
            if (appointment != null) {
                System.out.println("Appointment booked successfully: " + appointment);
            } else {
                System.out.println("Unable to book appointment at the requested time.");
                System.out.println("Doctor schedule now:");
                doctor.viewSchedule();
            }

            // -----------------------------
            // Βήμα 6: Διαβάζουμε Patients από αρχείο (8 πεδία/γραμμή)
            // -----------------------------
            try {
                System.out.println("\n=== Create Patients from file ===");
                List<Patient> patientsFromFile = readPatientsFromFile(Path.of("patients.txt"));
                for (Patient p : patientsFromFile) {
                    System.out.println("Loaded patient: " + p);
                    p.registration();
                }
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
            }

            System.out.println();
            System.out.println("=== Summary ===");
            System.out.println(patient);
            System.out.println(doctor);
            System.out.println(admin);
            System.out.println("Registered doctors (via admin):");
            admin.listDoctors();

            System.out.println();
            System.out.println("Program ended. Thank you!");

        } catch (IllegalArgumentException | InputMismatchException ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            scanner.close();
        }
    }

    // ----------------------------------------------------
    // Helper methods για input & parsing
    // ----------------------------------------------------

    private static String readNonEmpty(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("  Value cannot be empty. Please try again.");
        }
    }

    private static String readPattern(Scanner sc, String prompt, String regex, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.matches(regex)) return s;
            System.out.println("  " + errorMsg);
        }
    }

    private static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(line);
                if (n > 0) return n;
            } catch (NumberFormatException ignored) { }
            System.out.println("  Please enter a positive integer.");
        }
    }

    private static int readPositiveIntAllowZero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(line);
                if (n >= 0) return n;
            } catch (NumberFormatException ignored) { }
            System.out.println("  Please enter a non-negative integer (>= 0).");
        }
    }

    private static LocalDateTime readDateTime(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return LocalDateTime.parse(s, DATETIME_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("  Invalid date/time format. Use exactly: dd/MM/yyyy HH:mm");
            }
        }
    }

    private static LocalDate readLocalDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return LocalDate.parse(s, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("  Invalid date format. Use exactly: dd/MM/yyyy");
            }
        }
    }

    private static Users.Gender readGender(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().toUpperCase();
            switch (s) {
                case "MALE": case "M": case "ΑΝΔΡΑΣ": return Users.Gender.MALE;
                case "FEMALE": case "F": case "ΓΥΝΑΙΚΑ": return Users.Gender.FEMALE;
                case "OTHER": case "O": case "ΑΛΛΟ": return Users.Gender.OTHER;
                default:
                    System.out.println("  Please enter MALE / FEMALE / OTHER");
            }
        }
    }

    private static boolean readBooleanYesNo(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().toUpperCase();
            if (s.equals("Y") || s.equals("YES") || s.equals("ΝΑΙ")) return true;
            if (s.equals("N") || s.equals("NO")  || s.equals("ΟΧΙ")) return false;
            System.out.println("  Please enter Y or N.");
        }
    }

    // ----------------------------------------------------
    // Βήμα 6: ανάγνωση πολλών ασθενών από txt αρχείο
    // Μορφή γραμμής: username password name surname gender amka dateOfBirth insuranceId
    // Παράδειγμα:
    // user1 12345 John Stoikos MALE 12345678910 15/05/1999 INS-001
    // ----------------------------------------------------
    private static List<Patient> readPatientsFromFile(Path path) throws IOException {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue; // αγνόηση κενών γραμμών
                String[] parts = line.split("\\s+");
                if (parts.length != 8) {
                    throw new IllegalArgumentException(
                            "Λάθος μορφή στη γραμμή " + lineNum +
                                    " (αναμένονται 8 πεδία: username password name surname gender amka dateOfBirth insuranceId)"
                    );
                }
                try {
                    String username = parts[0];
                    String password = parts[1];
                    String name     = parts[2];
                    String surname  = parts[3];
                    Users.Gender gender = parseGender(parts[4], lineNum);
                    String amka    = parts[5];
                    LocalDate dob  = LocalDate.parse(parts[6], DATE_FMT);
                    String insId   = parts[7];

                    Patient p = new Patient(username, password, name, surname,
                            gender, amka, dob, insId);
                    patients.add(p);
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    throw new IllegalArgumentException(
                            "Λάθος δεδομένα στη γραμμή " + lineNum + ": " + e.getMessage()
                    );
                }
            }
        }
        return patients;
    }

    private static Users.Gender parseGender(String token, int lineNum) {
        String s = token.trim().toUpperCase();
        switch (s) {
            case "MALE": case "M": case "ΑΝΔΡΑΣ": return Users.Gender.MALE;
            case "FEMALE": case "F": case "ΓΥΝΑΙΚΑ": return Users.Gender.FEMALE;
            case "OTHER": case "O": case "ΑΛΛΟ": return Users.Gender.OTHER;
            default:
                throw new IllegalArgumentException("Invalid gender at line " + lineNum +
                        " (accepted: MALE/FEMALE/OTHER).");
        }
    }
}
