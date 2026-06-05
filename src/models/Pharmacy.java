package models;

/**
 * Represents a pharmacy loaded from the pharmacies CSV file.
 */
public class Pharmacy {

    private String pharmacyId;
    private String pharmacyName;
    private String address;
    private String contactNumber;

    /**
     * Constructs a Pharmacy with all fields.
     */
    public Pharmacy(String pharmacyId, String pharmacyName, String address, String contactNumber) {
        this.pharmacyId = pharmacyId;
        this.pharmacyName = pharmacyName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public String getPharmacyId() { return pharmacyId; }
    public String getPharmacyName() { return pharmacyName; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }

    // ─── Setters ────────────────────────────────────────────────────────────

    public void setPharmacyId(String pharmacyId) { this.pharmacyId = pharmacyId; }
    public void setPharmacyName(String pharmacyName) { this.pharmacyName = pharmacyName; }
    public void setAddress(String address) { this.address = address; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    @Override
    public String toString() {
        return "[" + pharmacyId + "] " + pharmacyName
                + " | " + address
                + " | Tel: " + contactNumber;
    }
}
