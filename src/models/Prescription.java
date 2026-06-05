package models;

import enums.PrescriptionStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all prescription types.
 * Holds common data and behavior shared by NormalPrescription and ControlledPrescription.
 */
public abstract class Prescription {

    protected String prescriptionId;
    protected Patient patient;
    protected Doctor doctor;
    protected List<Medication> medications;
    protected PrescriptionStatus status;
    protected LocalDate issueDate;
    protected LocalDate expirationDate;
    protected boolean refillRequested;

    /**
     * Constructs a Prescription with core fields.
     */
    public Prescription(String prescriptionId, Patient patient, Doctor doctor) {
        this.prescriptionId = prescriptionId;
        this.patient = patient;
        this.doctor = doctor;
        this.medications = new ArrayList<>();
        this.status = PrescriptionStatus.CREATED;
        this.issueDate = LocalDate.now();
        this.expirationDate = issueDate.plusMonths(3);
        this.refillRequested = false;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public String getPrescriptionId() { return prescriptionId; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public List<Medication> getMedications() { return medications; }
    public PrescriptionStatus getStatus() { return status; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public boolean isRefillRequested() { return refillRequested; }

    // ─── Setters ────────────────────────────────────────────────────────────

    public void setStatus(PrescriptionStatus status) { this.status = status; }
    public void setRefillRequested(boolean refillRequested) { this.refillRequested = refillRequested; }

    /**
     * Adds a medication to this prescription.
     *
     * @param medication the medication to add
     */
    public void addMedication(Medication medication) {
        medications.add(medication);
    }

    /**
     * Marks this prescription as having a refill requested.
     */
    public void requestRefill() {
        this.refillRequested = true;
        System.out.println("  Refill requested for prescription [" + prescriptionId + "]");
    }

    /**
     * Updates the status of this prescription.
     *
     * @param newStatus the new status to apply
     */
    public void changeStatus(PrescriptionStatus newStatus) {
        System.out.println("  Prescription [" + prescriptionId + "] status: "
                + this.status + " → " + newStatus);
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return "[" + prescriptionId + "] "
                + getClass().getSimpleName()
                + " | Patient: " + patient.getFullName()
                + " | Doctor: Dr. " + doctor.getFullName()
                + " | Status: " + status
                + " | Issued: " + issueDate
                + " | Expires: " + expirationDate
                + " | Medications: " + medications.size();
    }
}
