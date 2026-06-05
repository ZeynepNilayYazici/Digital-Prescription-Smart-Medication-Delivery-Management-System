package models;

import patterns.PrescriptionFactory;
import enums.PrescriptionStatus;

/**
 * Represents a doctor in the system.
 * Doctors can create and approve prescriptions.
 */
public class Doctor extends User {

    private String specialization;
    private String licenseNumber;

    /**
     * Constructs a Doctor with all required fields.
     */
    public Doctor(String id, String firstName, String lastName, String gender,
                  String dateOfBirth, String specialization, String licenseNumber) {
        super(id, firstName, lastName, gender, dateOfBirth);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public String getSpecialization() { return specialization; }
    public String getLicenseNumber() { return licenseNumber; }

    // ─── Setters ────────────────────────────────────────────────────────────

    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    /**
     * Creates a new prescription of the given type for the given patient.
     *
     * @param type    "NORMAL" or "CONTROLLED"
     * @param patient the patient for whom the prescription is created
     * @return a new Prescription object
     */
    public Prescription createPrescription(String type, Patient patient) {
        Prescription prescription = PrescriptionFactory.createPrescription(type, patient, this);
        System.out.println("  Dr. " + getFullName() + " created a " + type
                + " prescription [" + prescription.getPrescriptionId() + "] for "
                + patient.getFullName());
        return prescription;
    }

    /**
     * Grants additional approval for a controlled prescription.
     *
     * @param prescription the controlled prescription to approve
     */
    public void approveControlledPrescription(ControlledPrescription prescription) {
        prescription.grantAdditionalApproval();
        System.out.println("  Dr. " + getFullName()
                + " granted additional approval for controlled prescription ["
                + prescription.getPrescriptionId() + "]");
    }

    @Override
    public String toString() {
        return super.toString()
                + "\n    Specialization: " + specialization
                + " | License: " + licenseNumber;
    }
}
