package patterns;

import models.ControlledPrescription;
import models.Doctor;
import models.NormalPrescription;
import models.Patient;
import models.Prescription;
import utils.SystemUtils;

/**
 * Factory class implementing the Factory Method Pattern.
 * Creates the appropriate Prescription subclass based on the given type string.
 *
 * Supported types:
 *   - "NORMAL"     → NormalPrescription
 *   - "CONTROLLED" → ControlledPrescription
 */
public class PrescriptionFactory {

    /**
     * Creates and returns a Prescription of the specified type.
     * Direct constructor calls from outside should be avoided; use this factory instead.
     *
     * @param type    "NORMAL" or "CONTROLLED" (case-insensitive)
     * @param patient the patient the prescription is for
     * @param doctor  the doctor issuing the prescription
     * @return a new Prescription subclass instance
     * @throws IllegalArgumentException if the type is unrecognised
     */
    public static Prescription createPrescription(String type, Patient patient, Doctor doctor) {
        String prescriptionId = SystemUtils.generatePrescriptionId();

        switch (type.toUpperCase()) {
            case "NORMAL":
                return new NormalPrescription(prescriptionId, patient, doctor);
            case "CONTROLLED":
                return new ControlledPrescription(prescriptionId, patient, doctor);
            default:
                throw new IllegalArgumentException("Unknown prescription type: " + type);
        }
    }
}
