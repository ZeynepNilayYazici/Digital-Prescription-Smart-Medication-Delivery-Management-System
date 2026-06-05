package models;

/**
 * Represents a medication loaded from the medications CSV file.
 */
public class Medication {

    private String medicationId;
    private String medicationName;
    private String activeIngredients;
    private String allergenicCompounds;
    private String prescriptionType; // "NORMAL" or "CONTROLLED"

    /**
     * Constructs a Medication with all fields.
     */
    public Medication(String medicationId, String medicationName,
                      String activeIngredients, String allergenicCompounds,
                      String prescriptionType) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.activeIngredients = activeIngredients;
        this.allergenicCompounds = allergenicCompounds;
        this.prescriptionType = prescriptionType;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public String getMedicationId() { return medicationId; }
    public String getMedicationName() { return medicationName; }
    public String getActiveIngredients() { return activeIngredients; }
    public String getAllergenicCompounds() { return allergenicCompounds; }
    public String getPrescriptionType() { return prescriptionType; }

    // ─── Setters ────────────────────────────────────────────────────────────

    public void setMedicationId(String medicationId) { this.medicationId = medicationId; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
    public void setActiveIngredients(String activeIngredients) { this.activeIngredients = activeIngredients; }
    public void setAllergenicCompounds(String allergenicCompounds) { this.allergenicCompounds = allergenicCompounds; }
    public void setPrescriptionType(String prescriptionType) { this.prescriptionType = prescriptionType; }

    /**
     * Returns true if this medication is of CONTROLLED type.
     */
    public boolean isControlled() {
        return "CONTROLLED".equalsIgnoreCase(prescriptionType);
    }

    @Override
    public String toString() {
        return "[" + medicationId + "] " + medicationName
                + " | Active: " + activeIngredients
                + " | Allergens: " + allergenicCompounds
                + " | Type: " + prescriptionType;
    }
}
