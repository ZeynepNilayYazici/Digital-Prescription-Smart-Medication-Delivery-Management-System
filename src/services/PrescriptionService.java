package services;

import enums.PrescriptionStatus;
import models.Medication;
import models.Patient;
import models.Prescription;

/**
 * Service responsible for core prescription lifecycle management.
 *
 * Handles creation, cancellation, approval, and refill requests.
 */
public class PrescriptionService {

    /**
     * Registers a prescription with its patient (adds to history).
     *
     * @param prescription the prescription to register
     */
    public void createPrescription(Prescription prescription) {
        Patient patient = prescription.getPatient();
        patient.addPrescription(prescription);
        System.out.println("  [PrescriptionService] Prescription ["
                + prescription.getPrescriptionId() + "] registered for "
                + patient.getFullName());
    }

    /**
     * Cancels a prescription by setting its status to CANCELLED.
     *
     * @param prescription the prescription to cancel
     */
    public void cancelPrescription(Prescription prescription) {
        prescription.changeStatus(PrescriptionStatus.CANCELLED);
        System.out.println("  [PrescriptionService] Prescription ["
                + prescription.getPrescriptionId() + "] has been CANCELLED.");
    }

    /**
     * Approves a prescription by setting its status to APPROVED.
     *
     * @param prescription the prescription to approve
     */
    public void approvePrescription(Prescription prescription) {
        prescription.changeStatus(PrescriptionStatus.APPROVED);
        System.out.println("  [PrescriptionService] Prescription ["
                + prescription.getPrescriptionId() + "] has been APPROVED.");
    }

    /**
     * Flags a prescription for refill.
     *
     * @param prescription the prescription to refill
     */
    public void requestRefill(Prescription prescription) {
        prescription.requestRefill();
        System.out.println("  [PrescriptionService] Refill requested for ["
                + prescription.getPrescriptionId() + "].");
    }
}
