package models;

import enums.PrescriptionStatus;

/**
 * Represents a pharmacist in the system.
 * Pharmacists verify and dispense prescriptions.
 */
public class Pharmacist extends User {

    private String pharmacyId;

    /**
     * Constructs a Pharmacist with all required fields.
     */
    public Pharmacist(String id, String firstName, String lastName, String gender,
                      String dateOfBirth, String pharmacyId) {
        super(id, firstName, lastName, gender, dateOfBirth);
        this.pharmacyId = pharmacyId;
    }

    // ─── Getters & Setters ──────────────────────────────────────────────────

    public String getPharmacyId() { return pharmacyId; }
    public void setPharmacyId(String pharmacyId) { this.pharmacyId = pharmacyId; }

    /**
     * Verifies that a prescription is in APPROVED status and ready for dispensing.
     *
     * @param prescription the prescription to verify
     * @return true if the prescription is valid and approved
     */
    public boolean verifyPrescription(Prescription prescription) {
        if (prescription.getStatus() == PrescriptionStatus.APPROVED) {
            System.out.println("  Pharmacist " + getFullName()
                    + " verified prescription [" + prescription.getPrescriptionId() + "] — OK");
            return true;
        }
        System.out.println("  Pharmacist " + getFullName()
                + " could NOT verify prescription [" + prescription.getPrescriptionId()
                + "] — Status: " + prescription.getStatus());
        return false;
    }

    /**
     * Dispenses medication for a verified prescription, updating its status to DISPENSED.
     *
     * @param prescription the prescription to dispense
     */
    public void dispenseMedication(Prescription prescription) {
        if (verifyPrescription(prescription)) {
            prescription.changeStatus(PrescriptionStatus.DISPENSED);
            System.out.println("  Pharmacist " + getFullName()
                    + " dispensed medications for prescription ["
                    + prescription.getPrescriptionId() + "]");
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\n    Pharmacy ID: " + pharmacyId;
    }
}
