package patterns;

import enums.DeliveryStatus;
import enums.PrescriptionStatus;
import models.*;
import services.*;

/**
 * Facade class that hides the complexity of the prescription workflow.
 *
 * Instead of calling multiple services manually, clients call a single
 * high-level method on this facade. Implements the Facade Pattern.
 *
 * Workflow steps internally coordinated:
 *   1. Validate prescription (allergy check, approval check)
 *   2. Approve prescription
 *   3. Send notifications
 *   4. Create delivery request if patient is eligible
 */
public class PrescriptionWorkflowFacade {

    private final PrescriptionValidationService validationService;
    private final PrescriptionService prescriptionService;
    private final MedicationFulfillmentService fulfillmentService;
    private final NotificationManager notificationManager;

    /**
     * Constructs the facade, wiring together all required services.
     */
    public PrescriptionWorkflowFacade(PrescriptionValidationService validationService,
                                       PrescriptionService prescriptionService,
                                       MedicationFulfillmentService fulfillmentService,
                                       NotificationManager notificationManager) {
        this.validationService = validationService;
        this.prescriptionService = prescriptionService;
        this.fulfillmentService = fulfillmentService;
        this.notificationManager = notificationManager;
    }

    /**
     * Processes the full prescription workflow from validation through delivery.
     *
     * @param prescription   the prescription to process
     * @param requestDelivery true if the patient wants home delivery
     * @return true if the workflow completed successfully; false if validation failed
     */
    public boolean processPrescriptionWorkflow(Prescription prescription, boolean requestDelivery) {
        System.out.println("\n  --- Workflow Start: " + prescription.getPrescriptionId() + " ---");

        // Step 1: Validate
        boolean valid = validationService.validatePrescription(prescription);
        if (!valid) {
            prescriptionService.cancelPrescription(prescription);
            notificationManager.sendNotification("Prescription ["
                    + prescription.getPrescriptionId() + "] was REJECTED due to validation failure.");
            System.out.println("  --- Workflow End (REJECTED) ---");
            return false;
        }

        // Step 2: Approve
        prescriptionService.approvePrescription(prescription);
        notificationManager.sendNotification("Prescription ["
                + prescription.getPrescriptionId() + "] has been APPROVED.");

        // Step 3: Delivery (if eligible and requested)
        if (requestDelivery) {
            Patient patient = prescription.getPatient();
            if (patient.isDeliveryEligible()) {
                DeliveryRequest delivery = fulfillmentService.createDeliveryRequest(
                        prescription, patient, patient.getAddress());
                // Simulate delivery status progression
                fulfillmentService.updateDeliveryStatus(delivery, DeliveryStatus.PREPARING);
                notificationManager.sendNotification("Delivery [" + delivery.getDeliveryId()
                        + "] is now PREPARING.");
                fulfillmentService.updateDeliveryStatus(delivery, DeliveryStatus.OUT_FOR_DELIVERY);
                notificationManager.sendNotification("Delivery [" + delivery.getDeliveryId()
                        + "] is OUT FOR DELIVERY.");
                fulfillmentService.updateDeliveryStatus(delivery, DeliveryStatus.DELIVERED);
                notificationManager.sendNotification("Delivery [" + delivery.getDeliveryId()
                        + "] has been DELIVERED to " + patient.getFullName() + ".");
            } else {
                System.out.println("  Patient " + patient.getFullName()
                        + " is NOT eligible for home delivery.");
            }
        }

        System.out.println("  --- Workflow End (SUCCESS) ---");
        return true;
    }
}
