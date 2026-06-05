package models;

/**
 * Abstract base class representing a system user.
 * All user types (Patient, Doctor, Pharmacist) extend this class.
 */
public abstract class User {

    protected String id;
    protected String firstName;
    protected String lastName;
    protected String gender;
    protected String dateOfBirth;

    /**
     * Constructs a User with the given details.
     */
    public User(String id, String firstName, String lastName, String gender, String dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public String getDateOfBirth() { return dateOfBirth; }

    // ─── Setters ────────────────────────────────────────────────────────────

    public void setId(String id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    /** Returns the user's full name. */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + getFullName() + " (" + gender + ", DOB: " + dateOfBirth + ")";
    }
}
