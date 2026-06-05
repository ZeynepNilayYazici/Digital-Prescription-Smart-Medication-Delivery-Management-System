package services;

import enums.DeliveryStatus;
import enums.PrescriptionStatus;
import models.DeliveryRequest;
import models.Patient;
import models.Prescription;
import utils.SystemUtils;

/**
 * Service responsible for verifying prescriptions,
 * creating delivery requests, and tracking delivery status.
 */
public class MedicationFulfillmentService {

    /**
     * Verifies whether a prescription is in APPROVED status.
     *
     * @param prescription the prescription to verify
     * @return true if verified; false otherwise
     */
    public boolean verifyPrescription(Prescription prescription) {
        boolean approved = prescription.getStatus() == PrescriptionStatus.APPROVED;
        System.out.println("  [Fulfillment] Prescription ["
                + prescription.getPrescriptionId()
                + "] verification: " + (approved ? "PASSED" : "FAILED"));
        return approved;
    }

    /**
     * Creates a new DeliveryRequest for a given prescription and patient.
     *
     * @param prescription    the approved prescription
     * @param patient         the patient requesting delivery
     * @param deliveryAddress the address to deliver to
     * @return the newly created DeliveryRequest
     */
    public DeliveryRequest createDeliveryRequest(Prescription prescription,
                                                  Patient patient,
                                                  String deliveryAddress) {
        String deliveryId = SystemUtils.generateDeliveryId();
        DeliveryRequest delivery = new DeliveryRequest(deliveryId, prescription, patient, deliveryAddress);
        System.out.println("  [Fulfillment] Delivery request created: " + delivery.getDeliveryId()
                + " for " + patient.getFullName()
                + " → " + deliveryAddress);
        return delivery;
    }

    /**
     * Updates the status of a delivery request.
     *
     * @param delivery  the delivery request to update
     * @param newStatus the new delivery status
     */
    public void updateDeliveryStatus(DeliveryRequest delivery, DeliveryStatus newStatus) {
        delivery.updateStatus(newStatus);
    }

    /**
     * Marks a prescription as DISPENSED.
     *
     * @param prescription the prescription to dispense
     */
    public void dispenseMedication(Prescription prescription) {
        prescription.changeStatus(PrescriptionStatus.DISPENSED);
        System.out.println("  [Fulfillment] Medications dispensed for prescription ["
                + prescription.getPrescriptionId() + "].");
    }
}
