import models.*;
import enums.*;
import services.*;
import patterns.*;
import utils.*;

import java.util.*;

/**
 * Interactive console application for the
 * Digital Prescription & Smart Medication Delivery Management System.
 *
 * Users can log in as a Patient, Doctor, or Pharmacist and perform
 * real actions through a menu-driven interface.
 */
public class Main {

    // ─── Shared state ────────────────────────────────────────────────────────
    static Scanner scanner = new Scanner(System.in);
    static DataManager dataManager;
    static PrescriptionService prescriptionService;
    static PrescriptionValidationService validationService;
    static MedicationFulfillmentService fulfillmentService;
    static NotificationManager notificationManager;
    static PrescriptionWorkflowFacade facade;

    // Prescriptions created during this session (shared between roles)
    static List<Prescription> sessionPrescriptions = new ArrayList<>();

    // =========================================================================
    // MAIN
    // =========================================================================
    public static void main(String[] args) {

        // ── Boot up ──────────────────────────────────────────────────────────
        printBanner();
        String dataDir = resolveDataDirectory();
        dataManager         = new DataManager(new CSVManager(dataDir));
        dataManager.loadAll();

        prescriptionService = new PrescriptionService();
        validationService   = new PrescriptionValidationService();
        fulfillmentService  = new MedicationFulfillmentService();
        notificationManager = new NotificationManager();

        NotificationObserver patientPortal  = new NotificationObserver("Patient Portal");
        NotificationObserver pharmacySys    = new NotificationObserver("Pharmacy System");
        notificationManager.addObserver(patientPortal);
        notificationManager.addObserver(pharmacySys);

        facade = new PrescriptionWorkflowFacade(
                validationService, prescriptionService, fulfillmentService, notificationManager);

        // ── Role selection loop ───────────────────────────────────────────────
        while (true) {
            printLine();
            System.out.println("  WHO ARE YOU?");
            printLine();
            System.out.println("  [1] Patient");
            System.out.println("  [2] Doctor");
            System.out.println("  [3] Pharmacist");
            System.out.println("  [0] Exit");
            printLine();
            String role = prompt("Enter your choice");

            switch (role) {
                case "1": patientLogin();     break;
                case "2": doctorLogin();      break;
                case "3": pharmacistLogin();  break;
                case "0":
                    System.out.println("\n  Goodbye!\n");
                    return;
                default:
                    System.out.println("  Invalid choice. Please try again.");
            }
        }
    }

    // =========================================================================
    // PATIENT FLOW
    // =========================================================================
    static void patientLogin() {
        printLine();
        System.out.println("  PATIENT LOGIN");
        printLine();
        System.out.println("  Available patients:");
        for (Patient p : dataManager.getPatients()) {
            System.out.println("    ID: " + p.getId()
                    + "  Name: " + p.getFullName()
                    + "  Allergies: " + (p.getAllergies().isEmpty() ? "None" : String.join(", ", p.getAllergies())));
        }
        printLine();
        String id = prompt("Enter your Patient ID (e.g. P001)");
        Patient patient = dataManager.findPatientById(id);

        if (patient == null) {
            System.out.println("  Patient not found. Going back.");
            return;
        }

        System.out.println("\n  Welcome, " + patient.getFullName() + "!");

        while (true) {
            printLine();
            System.out.println("  PATIENT MENU — " + patient.getFullName());
            printLine();
            System.out.println("  [1] View my prescription history");
            System.out.println("  [2] View my profile & allergies");
            System.out.println("  [3] Request a refill on a prescription");
            System.out.println("  [4] Request home delivery for a prescription");
            System.out.println("  [0] Logout");
            printLine();
            String choice = prompt("Enter your choice");

            switch (choice) {
                case "1": patientViewHistory(patient);       break;
                case "2": patientViewProfile(patient);       break;
                case "3": patientRequestRefill(patient);     break;
                case "4": patientRequestDelivery(patient);   break;
                case "0": System.out.println("  Logged out.\n"); return;
                default:  System.out.println("  Invalid choice.");
            }
        }
    }

    static void patientViewHistory(Patient patient) {
        printLine();
        System.out.println("  PRESCRIPTION HISTORY — " + patient.getFullName());
        printLine();
        List<Prescription> history = patient.getPrescriptionHistory();
        if (history.isEmpty()) {
            System.out.println("  You have no prescriptions yet.");
            return;
        }
        for (Prescription rx : history) {
            System.out.println("  ID      : " + rx.getPrescriptionId());
            System.out.println("  Type    : " + rx.getClass().getSimpleName());
            System.out.println("  Doctor  : Dr. " + rx.getDoctor().getFullName());
            System.out.println("  Status  : " + rx.getStatus());
            System.out.println("  Issued  : " + rx.getIssueDate());
            System.out.println("  Expires : " + rx.getExpirationDate());
            System.out.println("  Medications:");
            for (Medication m : rx.getMedications()) {
                System.out.println("    - " + m.getMedicationName() + " (" + m.getPrescriptionType() + ")");
            }
            System.out.println("  Refill Requested: " + (rx.isRefillRequested() ? "Yes" : "No"));
            printLine();
        }
    }

    static void patientViewProfile(Patient patient) {
        printLine();
        System.out.println("  MY PROFILE");
        printLine();
        System.out.println("  Name     : " + patient.getFullName());
        System.out.println("  Gender   : " + patient.getGender());
        System.out.println("  DOB      : " + patient.getDateOfBirth());
        System.out.println("  Age      : " + SystemUtils.calculateAge(patient.getDateOfBirth()));
        System.out.println("  Address  : " + patient.getAddress());
        System.out.println("  Phone    : " + patient.getPhoneNumber());
        System.out.println("  Allergies: " + (patient.getAllergies().isEmpty() ? "None" : String.join(", ", patient.getAllergies())));
        System.out.println("  Home Delivery Eligible: " + (patient.isDeliveryEligible() ? "YES — " + patient.getEligibilityReason() : "NO"));
    }

    static void patientRequestRefill(Patient patient) {
        printLine();
        System.out.println("  REQUEST A REFILL");
        printLine();
        List<Prescription> approved = getPrescriptionsByStatus(patient, PrescriptionStatus.APPROVED);
        if (approved.isEmpty()) {
            System.out.println("  You have no approved prescriptions available for refill.");
            return;
        }
        System.out.println("  Your approved prescriptions:");
        listPrescriptions(approved);
        String rxId = prompt("Enter prescription ID to request refill");
        Prescription rx = findPrescriptionInList(approved, rxId);
        if (rx == null) {
            System.out.println("  Prescription not found.");
            return;
        }
        prescriptionService.requestRefill(rx);
        System.out.println("  Refill request submitted successfully!");
        notificationManager.sendNotification("Refill requested for prescription [" + rx.getPrescriptionId() + "] by " + patient.getFullName());
    }

    static void patientRequestDelivery(Patient patient) {
        printLine();
        System.out.println("  REQUEST HOME DELIVERY");
        printLine();
        if (!patient.isDeliveryEligible()) {
            System.out.println("  Sorry, you are not eligible for home delivery.");
            return;
        }
        System.out.println("  You are eligible for home delivery. Reason: " + patient.getEligibilityReason());
        List<Prescription> dispensed = getPrescriptionsByStatus(patient, PrescriptionStatus.DISPENSED);
        List<Prescription> approved  = getPrescriptionsByStatus(patient, PrescriptionStatus.APPROVED);
        List<Prescription> eligible  = new ArrayList<>();
        eligible.addAll(approved);
        eligible.addAll(dispensed);
        if (eligible.isEmpty()) {
            System.out.println("  No approved or dispensed prescriptions available for delivery.");
            return;
        }
        System.out.println("  Eligible prescriptions:");
        listPrescriptions(eligible);
        String rxId = prompt("Enter prescription ID for delivery");
        Prescription rx = findPrescriptionInList(eligible, rxId);
        if (rx == null) {
            System.out.println("  Prescription not found.");
            return;
        }
        System.out.println("  Your registered address: " + patient.getAddress());
        String useDefault = prompt("Deliver to this address? (yes / no)");
        String address = patient.getAddress();
        if (useDefault.equalsIgnoreCase("no")) {
            address = prompt("Enter delivery address");
        }
        // Create delivery and simulate progression
        models.DeliveryRequest delivery = fulfillmentService.createDeliveryRequest(rx, patient, address);
        System.out.println("\n  Simulating delivery progress...");
        pause();
        fulfillmentService.updateDeliveryStatus(delivery, DeliveryStatus.PREPARING);
        notificationManager.sendNotification("Delivery [" + delivery.getDeliveryId() + "] is PREPARING.");
        pause();
        fulfillmentService.updateDeliveryStatus(delivery, DeliveryStatus.OUT_FOR_DELIVERY);
        notificationManager.sendNotification("Delivery [" + delivery.getDeliveryId() + "] is OUT FOR DELIVERY.");
        pause();
        fulfillmentService.updateDeliveryStatus(delivery, DeliveryStatus.DELIVERED);
        notificationManager.sendNotification("Delivery [" + delivery.getDeliveryId() + "] has been DELIVERED to " + patient.getFullName() + "!");
        System.out.println("\n  Your medication has been delivered successfully!");
    }

    // =========================================================================
    // DOCTOR FLOW
    // =========================================================================
    static void doctorLogin() {
        printLine();
        System.out.println("  DOCTOR LOGIN");
        printLine();
        System.out.println("  Available doctors:");
        for (Doctor d : dataManager.getDoctors()) {
            System.out.println("    ID: " + d.getId()
                    + "  Name: Dr. " + d.getFullName()
                    + "  Specialization: " + d.getSpecialization());
        }
        printLine();
        String id = prompt("Enter your Doctor ID (e.g. D001)");
        Doctor doctor = dataManager.findDoctorById(id);

        if (doctor == null) {
            System.out.println("  Doctor not found. Going back.");
            return;
        }

        System.out.println("\n  Welcome, Dr. " + doctor.getFullName() + "!");

        while (true) {
            printLine();
            System.out.println("  DOCTOR MENU — Dr. " + doctor.getFullName());
            printLine();
            System.out.println("  [1] Create a new prescription for a patient");
            System.out.println("  [2] Approve a controlled prescription");
            System.out.println("  [3] View all prescriptions (this session)");
            System.out.println("  [0] Logout");
            printLine();
            String choice = prompt("Enter your choice");

            switch (choice) {
                case "1": doctorCreatePrescription(doctor); break;
                case "2": doctorApproveControlled(doctor);  break;
                case "3": doctorViewAllPrescriptions();      break;
                case "0": System.out.println("  Logged out.\n"); return;
                default:  System.out.println("  Invalid choice.");
            }
        }
    }

    static void doctorCreatePrescription(Doctor doctor) {
        printLine();
        System.out.println("  CREATE PRESCRIPTION");
        printLine();

        // Pick patient
        System.out.println("  Available patients:");
        for (Patient p : dataManager.getPatients()) {
            System.out.println("    ID: " + p.getId() + "  Name: " + p.getFullName()
                    + "  Allergies: " + (p.getAllergies().isEmpty() ? "None" : String.join(", ", p.getAllergies())));
        }
        String patientId = prompt("Enter Patient ID");
        Patient patient = dataManager.findPatientById(patientId);
        if (patient == null) {
            System.out.println("  Patient not found.");
            return;
        }

        // Pick medications
        System.out.println("\n  Available medications:");
        for (Medication m : dataManager.getMedications()) {
            System.out.println("    ID: " + m.getMedicationId()
                    + "  Name: " + m.getMedicationName()
                    + "  Type: " + m.getPrescriptionType()
                    + "  Allergens: " + m.getAllergenicCompounds());
        }

        List<Medication> chosen = new ArrayList<>();
        boolean hasControlled = false;
        while (true) {
            String medId = prompt("Enter Medication ID to add (or 'done' to finish)");
            if (medId.equalsIgnoreCase("done")) {
                if (chosen.isEmpty()) {
                    System.out.println("  You must add at least one medication.");
                } else {
                    break;
                }
            } else {
                Medication med = dataManager.findMedicationById(medId);
                if (med == null) {
                    System.out.println("  Medication not found. Try again.");
                } else {
                    chosen.add(med);
                    if (med.isControlled()) hasControlled = true;
                    System.out.println("  Added: " + med.getMedicationName());
                }
            }
        }

        // Determine prescription type
        String type = hasControlled ? "CONTROLLED" : "NORMAL";
        System.out.println("\n  Prescription type will be: " + type);
        if (hasControlled) {
            System.out.println("  (Because at least one medication is CONTROLLED)");
        }

        // Create prescription via factory
        Prescription rx = doctor.createPrescription(type, patient);
        for (Medication m : chosen) {
            rx.addMedication(m);
        }
        prescriptionService.createPrescription(rx);
        sessionPrescriptions.add(rx);

        // Run workflow
        if (type.equals("CONTROLLED")) {
            System.out.println("\n  This is a CONTROLLED prescription.");
            System.out.println("  You must grant additional approval before it can be processed.");
            String approve = prompt("  Grant additional approval now? (yes / no)");
            if (approve.equalsIgnoreCase("yes")) {
                doctor.approveControlledPrescription((ControlledPrescription) rx);
            } else {
                System.out.println("  Prescription created but NOT yet approved. Pharmacist cannot dispense it yet.");
                return;
            }
        }

        // Ask for delivery
        String wantsDelivery = "no";
        if (patient.isDeliveryEligible()) {
            wantsDelivery = prompt("Patient is delivery eligible. Request home delivery? (yes / no)");
        }

        boolean success = facade.processPrescriptionWorkflow(rx, wantsDelivery.equalsIgnoreCase("yes"));
        System.out.println("\n  Prescription " + (success ? "processed successfully!" : "was rejected (check warnings above)."));
    }

    static void doctorApproveControlled(Doctor doctor) {
        printLine();
        System.out.println("  APPROVE CONTROLLED PRESCRIPTION");
        printLine();
        List<Prescription> pending = new ArrayList<>();
        for (Prescription rx : sessionPrescriptions) {
            if (rx instanceof ControlledPrescription) {
                ControlledPrescription crx = (ControlledPrescription) rx;
                if (!crx.isAdditionalApprovalGranted()) {
                    pending.add(rx);
                }
            }
        }
        if (pending.isEmpty()) {
            System.out.println("  No controlled prescriptions are waiting for approval.");
            return;
        }
        System.out.println("  Controlled prescriptions awaiting approval:");
        listPrescriptions(pending);
        String rxId = prompt("Enter prescription ID to approve");
        Prescription found = findPrescriptionInList(pending, rxId);
        if (found == null) {
            System.out.println("  Prescription not found.");
            return;
        }
        doctor.approveControlledPrescription((ControlledPrescription) found);
        System.out.println("  Approval granted. This prescription can now be processed.");
    }

    static void doctorViewAllPrescriptions() {
        printLine();
        System.out.println("  ALL PRESCRIPTIONS — THIS SESSION");
        printLine();
        if (sessionPrescriptions.isEmpty()) {
            System.out.println("  No prescriptions created yet this session.");
            return;
        }
        for (Prescription rx : sessionPrescriptions) {
            System.out.println("  " + rx);
        }
    }

    // =========================================================================
    // PHARMACIST FLOW
    // =========================================================================
    static void pharmacistLogin() {
        printLine();
        System.out.println("  PHARMACIST LOGIN");
        printLine();
        System.out.println("  Available pharmacies:");
        for (models.Pharmacy ph : dataManager.getPharmacies()) {
            System.out.println("    ID: " + ph.getPharmacyId() + "  Name: " + ph.getPharmacyName()
                    + "  Address: " + ph.getAddress());
        }
        printLine();
        // Simple login: pharmacist enters their name and pharmacy ID
        String name       = prompt("Enter your name");
        String pharmacyId = prompt("Enter your Pharmacy ID (e.g. PH001)");
        models.Pharmacy pharmacy = dataManager.findPharmacyById(pharmacyId);
        if (pharmacy == null) {
            System.out.println("  Pharmacy not found. Going back.");
            return;
        }
        Pharmacist pharmacist = new Pharmacist(
                "PH-STAFF", name, "", "N/A", "N/A", pharmacyId);

        System.out.println("\n  Welcome, " + name + " from " + pharmacy.getPharmacyName() + "!");

        while (true) {
            printLine();
            System.out.println("  PHARMACIST MENU — " + pharmacy.getPharmacyName());
            printLine();
            System.out.println("  [1] View all prescriptions (this session)");
            System.out.println("  [2] Verify a prescription");
            System.out.println("  [3] Dispense medication for a prescription");
            System.out.println("  [0] Logout");
            printLine();
            String choice = prompt("Enter your choice");

            switch (choice) {
                case "1": doctorViewAllPrescriptions();              break;
                case "2": pharmacistVerify(pharmacist);              break;
                case "3": pharmacistDispense(pharmacist);            break;
                case "0": System.out.println("  Logged out.\n"); return;
                default:  System.out.println("  Invalid choice.");
            }
        }
    }

    static void pharmacistVerify(Pharmacist pharmacist) {
        printLine();
        System.out.println("  VERIFY PRESCRIPTION");
        printLine();
        if (sessionPrescriptions.isEmpty()) {
            System.out.println("  No prescriptions available.");
            return;
        }
        listPrescriptions(sessionPrescriptions);
        String rxId = prompt("Enter prescription ID to verify");
        Prescription rx = findPrescriptionInList(sessionPrescriptions, rxId);
        if (rx == null) {
            System.out.println("  Prescription not found.");
            return;
        }
        boolean ok = pharmacist.verifyPrescription(rx);
        if (ok) {
            System.out.println("  Prescription is APPROVED and ready to dispense.");
        } else {
            System.out.println("  Prescription cannot be dispensed. Status: " + rx.getStatus());
        }
    }

    static void pharmacistDispense(Pharmacist pharmacist) {
        printLine();
        System.out.println("  DISPENSE MEDICATION");
        printLine();
        List<Prescription> approved = new ArrayList<>();
        for (Prescription rx : sessionPrescriptions) {
            if (rx.getStatus() == PrescriptionStatus.APPROVED) approved.add(rx);
        }
        if (approved.isEmpty()) {
            System.out.println("  No approved prescriptions ready for dispensing.");
            return;
        }
        System.out.println("  Approved prescriptions:");
        listPrescriptions(approved);
        String rxId = prompt("Enter prescription ID to dispense");
        Prescription rx = findPrescriptionInList(approved, rxId);
        if (rx == null) {
            System.out.println("  Prescription not found.");
            return;
        }
        pharmacist.dispenseMedication(rx);
        notificationManager.sendNotification("Medications dispensed for prescription ["
                + rx.getPrescriptionId() + "] — Patient: " + rx.getPatient().getFullName());
        System.out.println("  Medication successfully dispensed!");
    }

    // =========================================================================
    // SHARED HELPERS
    // =========================================================================

    static String prompt(String message) {
        System.out.print("  > " + message + ": ");
        return scanner.nextLine().trim();
    }

    static void printLine() {
        System.out.println("  -------------------------------------------------------");
    }

    static void printBanner() {
        System.out.println();
        System.out.println("  =======================================================");
        System.out.println("   Digital Prescription & Smart Medication Delivery");
        System.out.println("   Management System");
        System.out.println("  =======================================================");
        System.out.println();
    }

    static void listPrescriptions(List<Prescription> list) {
        for (Prescription rx : list) {
            System.out.println("    ID: " + rx.getPrescriptionId()
                    + "  Patient: " + rx.getPatient().getFullName()
                    + "  Status: " + rx.getStatus()
                    + "  Type: " + rx.getClass().getSimpleName());
        }
    }

    static List<Prescription> getPrescriptionsByStatus(Patient patient, PrescriptionStatus status) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription rx : patient.getPrescriptionHistory()) {
            if (rx.getStatus() == status) result.add(rx);
        }
        return result;
    }

    static Prescription findPrescriptionInList(List<Prescription> list, String id) {
        for (Prescription rx : list) {
            if (rx.getPrescriptionId().equalsIgnoreCase(id)) return rx;
        }
        return null;
    }

    /** Small pause to make delivery simulation feel real. */
    static void pause() {
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
    }

    static String resolveDataDirectory() {
        java.io.File direct = new java.io.File("data");
        if (direct.exists() && direct.isDirectory()) return "data";
        java.io.File parent = new java.io.File("../data");
        if (parent.exists() && parent.isDirectory()) return "../data";
        return "/home/claude/project/data";
    }
}
