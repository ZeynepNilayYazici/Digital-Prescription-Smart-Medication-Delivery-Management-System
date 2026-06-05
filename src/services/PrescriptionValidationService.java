package services;

import models.ControlledPrescription;
import models.Medication;
import models.Patient;
import models.Prescription;

import java.util.List;

/**
 * Service responsible for validating prescriptions.
 *
 * Validation checks:
 *   1. Allergy conflict detection (patient allergies vs medication allergens)
 *   2. Controlled medication additional approval check
 */
public class PrescriptionValidationService {

    /**
     * Validates a prescription by running all checks.
     * Prints a summary of the validation result.
     *
     * @param prescription the prescription to validate
     * @return true if the prescription passes all checks; false otherwise
     */
    public boolean validatePrescription(Prescription prescription) {
        System.out.println("  [Validation] Validating prescription ["
                + prescription.getPrescriptionId() + "] ...");

        boolean allergyOk = !checkAllergyConflict(prescription);

        if (!allergyOk) {
            System.out.println("  [Validation] FAILED — allergy conflict detected.");
            return false;
        }

        if (requiresAdditionalApproval(prescription)) {
            ControlledPrescription controlled = (ControlledPrescription) prescription;
            if (!controlled.isAdditionalApprovalGranted()) {
                System.out.println("  [Validation] FAILED — controlled medication requires "
                        + "additional doctor approval.");
                return false;
            }
        }

        System.out.println("  [Validation] PASSED — prescription is valid.");
        return true;
    }

    /**
     * Checks whether any medication in the prescription conflicts with
     * the patient's known allergies.
     *
     * @param prescription the prescription to check
     * @return true if a conflict exists; false if safe
     */
    public boolean checkAllergyConflict(Prescription prescription) {
        Patient patient = prescription.getPatient();
        List<String> allergies = patient.getAllergies();

        if (allergies.isEmpty()) return false;

        boolean conflictFound = false;
        for (Medication med : prescription.getMedications()) {
            String allergens = med.getAllergenicCompounds();
            if (allergens == null || allergens.equalsIgnoreCase("None")) continue;

            for (String allergy : allergies) {
                if (allergens.toLowerCase().contains(allergy.toLowerCase())) {
                    System.out.println("  [Validation] ⚠ ALLERGY CONFLICT: Patient "
                            + patient.getFullName()
                            + " is allergic to '" + allergy
                            + "' — found in medication '" + med.getMedicationName()
                            + "' (allergens: " + allergens + ")");
                    conflictFound = true;
                }
            }
        }
        return conflictFound;
    }

    /**
     * Determines whether the prescription requires additional approval
     * (i.e. it is a ControlledPrescription).
     *
     * @param prescription the prescription to check
     * @return true if additional approval is required
     */
    public boolean requiresAdditionalApproval(Prescription prescription) {
        return prescription instanceof ControlledPrescription;
    }
}
