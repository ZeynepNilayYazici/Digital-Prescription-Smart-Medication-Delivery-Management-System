package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the system.
 * Stores personal information, allergy data, delivery eligibility,
 * and prescription history.
 */
public class Patient extends User {

    private String address;
    private String phoneNumber;
    private List<String> allergies;
    private boolean deliveryEligible;
    private String eligibilityReason;
    private List<Prescription> prescriptionHistory;

    /**
     * Constructs a Patient with all required fields.
     */
    public Patient(String id, String firstName, String lastName, String gender,
                   String dateOfBirth, String address, String phoneNumber,
                   List<String> allergies, boolean deliveryEligible, String eligibilityReason) {
        super(id, firstName, lastName, gender, dateOfBirth);
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.allergies = (allergies != null) ? allergies : new ArrayList<>();
        this.deliveryEligible = deliveryEligible;
        this.eligibilityReason = eligibilityReason;
        this.prescriptionHistory = new ArrayList<>();
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public List<String> getAllergies() { return allergies; }
    public boolean isDeliveryEligible() { return deliveryEligible; }
    public String getEligibilityReason() { return eligibilityReason; }
    public List<Prescription> getPrescriptionHistory() { return prescriptionHistory; }

    // ─── Setters ────────────────────────────────────────────────────────────

    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setDeliveryEligible(boolean deliveryEligible) { this.deliveryEligible = deliveryEligible; }
    public void setEligibilityReason(String eligibilityReason) { this.eligibilityReason = eligibilityReason; }

    /**
     * Adds a prescription to the patient's history.
     *
     * @param prescription the prescription to add
     */
    public void addPrescription(Prescription prescription) {
        prescriptionHistory.add(prescription);
    }

    /**
     * Prints all prescriptions in the patient's history.
     */
    public void viewPrescriptionHistory() {
        if (prescriptionHistory.isEmpty()) {
            System.out.println("  No prescriptions found for " + getFullName());
            return;
        }
        System.out.println("  Prescription history for " + getFullName() + ":");
        for (Prescription p : prescriptionHistory) {
            System.out.println("    - " + p.getPrescriptionId()
                    + " | Status: " + p.getStatus()
                    + " | Issued: " + p.getIssueDate());
        }
    }

    /**
     * Adds a new allergy to the patient's allergy list.
     *
     * @param allergy the allergy compound to add
     */
    public void addAllergy(String allergy) {
        if (!allergies.contains(allergy)) {
            allergies.add(allergy);
        }
    }

    @Override
    public String toString() {
        return super.toString()
                + "\n    Address: " + address
                + " | Phone: " + phoneNumber
                + " | Allergies: " + (allergies.isEmpty() ? "None" : String.join(", ", allergies))
                + " | Delivery Eligible: " + deliveryEligible
                + (deliveryEligible ? " (" + eligibilityReason + ")" : "");
    }
}
