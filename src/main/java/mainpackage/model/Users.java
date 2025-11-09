package mainpackage.model;

/**
 * Represents a generic user in the appointment management system.
 * <p>
 * Each user has a username, password and personal information such as
 * first name, surname and gender. The class also keeps track of the number
 * of users created via a static counter. Derived classes such as
 * {@link Patient}, {@link Doctor} and {@link Admin} extend this
 * class to add additional behaviour and attributes.
 */
public class Users {

    /** Gender of the user. */
    public enum Gender { MALE, FEMALE, OTHER }

    /**
     * A counter that increments every time a new user is created.
     * Because this field is static, all instances share the same counter value.
     */
    protected static int usersCounter = 0;

    /** The username used for logging in. */
    protected String username;

    /** The password associated with this user. */
    protected String password;

    /** The given name of the user. */
    protected String name;

    /** The family name of the user. */
    protected String surname;

    /** The gender of the user (required). */
    protected Gender gender;

    /**
     * Constructs a new user. When a user is constructed the
     * {@code usersCounter} is incremented.
     *
     * @param username the login username
     * @param password the login password
     * @param name     the first name
     * @param surname  the last name
     * @param gender   the gender
     */
    public Users(String username, String password, String name, String surname, Gender gender) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        usersCounter++;
    }

    /** Convenience overload: attempts login using the stored credentials. */
    public boolean login() {
        return login(this.username, this.password);
    }

    /**
     * Attempts to log in a user by comparing the supplied credentials with
     * this user's credentials.
     *
     * @param username the username supplied at login
     * @param password the password supplied at login
     * @return {@code true} if the credentials match this user, otherwise {@code false}
     */
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /** Logs the user out of the system. */
    public void logout() {
        System.out.println(this.name + " " + this.surname + " logged out.");
    }

    // Getters and setters
    public static int getUsersCounter() {
        return usersCounter;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
}
