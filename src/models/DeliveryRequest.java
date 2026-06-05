package models;

import enums.DeliveryStatus;

/**
 * Represents a home delivery request for a dispensed prescription.
 */
public class DeliveryRequest {

    private String deliveryId;
    private Prescription prescription;
    private Patient patient;
    private DeliveryStatus status;
    private String deliveryAddress;

    /**
     * Constructs a DeliveryRequest.
     *
     * @param deliveryId      unique delivery identifier
     * @param prescription    the prescription being delivered
     * @param patient         the patient receiving the delivery
     * @param deliveryAddress the delivery address
     */
    public DeliveryRequest(String deliveryId, Prescription prescription,
                           Patient patient, String deliveryAddress) {
        this.deliveryId = deliveryId;
        this.prescription = prescription;
        this.patient = patient;
        this.deliveryAddress = deliveryAddress;
        this.status = DeliveryStatus.REQUESTED;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public String getDeliveryId() { return deliveryId; }
    public Prescription getPrescription() { return prescription; }
    public Patient getPatient() { return patient; }
    public DeliveryStatus getStatus() { return status; }
    public String getDeliveryAddress() { return deliveryAddress; }

    // ─── Setters ────────────────────────────────────────────────────────────

    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    /**
     * Updates the delivery status and prints a status-change log.
     *
     * @param newStatus the new delivery status
     */
    public void updateStatus(DeliveryStatus newStatus) {
        System.out.println("  Delivery [" + deliveryId + "] status: "
                + this.status + " → " + newStatus);
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return "[" + deliveryId + "] Delivery for " + patient.getFullName()
                + " | Prescription: " + prescription.getPrescriptionId()
                + " | Address: " + deliveryAddress
                + " | Status: " + status;
    }
}
