package models;

/**
 * Represents a controlled prescription requiring additional doctor approval
 * before it can be fulfilled.
 */
public class ControlledPrescription extends Prescription {

    private boolean additionalApprovalGranted;

    /**
     * Constructs a ControlledPrescription.
     *
     * @param prescriptionId unique identifier
     * @param patient        the patient
     * @param doctor         the prescribing doctor
     */
    public ControlledPrescription(String prescriptionId, Patient patient, Doctor doctor) {
        super(prescriptionId, patient, doctor);
        this.additionalApprovalGranted = false;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public boolean isAdditionalApprovalGranted() { return additionalApprovalGranted; }

    /**
     * Grants additional approval for this controlled prescription.
     * Must be called by an authorized doctor before the prescription can be dispensed.
     */
    public void grantAdditionalApproval() {
        this.additionalApprovalGranted = true;
        System.out.println("  Additional approval granted for controlled prescription ["
                + prescriptionId + "]");
    }

    @Override
    public String toString() {
        return "CONTROLLED | " + super.toString()
                + " | Additional Approval: " + (additionalApprovalGranted ? "YES" : "PENDING");
    }
}
