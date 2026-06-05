package services;

import models.*;
import utils.CSVManager;

import java.util.List;

/**
 * Loads all CSV seed data at startup and provides lookup methods
 * for patients, doctors, medications, and pharmacies.
 */
public class DataManager {

    private List<Patient> patients;
    private List<Doctor> doctors;
    private List<Medication> medications;
    private List<Pharmacy> pharmacies;

    private final CSVManager csvManager;

    /**
     * Constructs a DataManager with the given CSVManager.
     *
     * @param csvManager the CSVManager used to read CSV files
     */
    public DataManager(CSVManager csvManager) {
        this.csvManager = csvManager;
    }

    /**
     * Loads all CSV files into memory. Must be called once at startup.
     */
    public void loadAll() {
        loadPatients();
        loadDoctors();
        loadMedications();
        loadPharmacies();
        System.out.println("  [DataManager] Loaded: "
                + patients.size() + " patients, "
                + doctors.size() + " doctors, "
                + medications.size() + " medications, "
                + pharmacies.size() + " pharmacies.");
    }

    /** Loads patients from CSV. */
    public void loadPatients() {
        patients = csvManager.readPatients();
    }

    /** Loads doctors from CSV. */
    public void loadDoctors() {
        doctors = csvManager.readDoctors();
    }

    /** Loads medications from CSV. */
    public void loadMedications() {
        medications = csvManager.readMedications();
    }

    /** Loads pharmacies from CSV. */
    public void loadPharmacies() {
        pharmacies = csvManager.readPharmacies();
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public List<Patient> getPatients() { return patients; }
    public List<Doctor> getDoctors() { return doctors; }
    public List<Medication> getMedications() { return medications; }
    public List<Pharmacy> getPharmacies() { return pharmacies; }

    // ─── Finders ────────────────────────────────────────────────────────────

    /**
     * Finds a Patient by ID.
     *
     * @param id the patient ID
     * @return the matching Patient or null
     */
    public Patient findPatientById(String id) {
        return patients.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /**
     * Finds a Doctor by ID.
     *
     * @param id the doctor ID
     * @return the matching Doctor or null
     */
    public Doctor findDoctorById(String id) {
        return doctors.stream()
                .filter(d -> d.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /**
     * Finds a Medication by ID.
     *
     * @param id the medication ID
     * @return the matching Medication or null
     */
    public Medication findMedicationById(String id) {
        return medications.stream()
                .filter(m -> m.getMedicationId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /**
     * Finds a Pharmacy by ID.
     *
     * @param id the pharmacy ID
     * @return the matching Pharmacy or null
     */
    public Pharmacy findPharmacyById(String id) {
        return pharmacies.stream()
                .filter(ph -> ph.getPharmacyId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }
}
