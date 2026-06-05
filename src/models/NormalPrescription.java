package models;

/**
 * Represents a standard (non-controlled) prescription.
 * No additional approval is required for this prescription type.
 */
public class NormalPrescription extends Prescription {

    /**
     * Constructs a NormalPrescription.
     *
     * @param prescriptionId unique identifier
     * @param patient        the patient
     * @param doctor         the prescribing doctor
     */
    public NormalPrescription(String prescriptionId, Patient patient, Doctor doctor) {
        super(prescriptionId, patient, doctor);
    }

    @Override
    public String toString() {
        return "NORMAL | " + super.toString();
    }
}
