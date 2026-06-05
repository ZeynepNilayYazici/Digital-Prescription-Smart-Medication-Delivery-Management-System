package utils;

import models.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Reads CSV seed data files and parses them into Java model objects.
 * CSV files are treated as read-only; no data is ever written back.
 */
public class CSVManager {

    private final String dataDirectory;

    /**
     * Constructs a CSVManager pointing to the given data directory.
     *
     * @param dataDirectory path to the folder containing the CSV files
     */
    public CSVManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    // ─── Public read methods ─────────────────────────────────────────────────

    /**
     * Reads patients.csv and returns a list of Patient objects.
     */
    public List<Patient> readPatients() {
        List<Patient> patients = new ArrayList<>();
        for (String[] fields : parseFile("patients.csv")) {
            if (fields.length < 10) continue;
            String id           = fields[0].trim();
            String firstName    = fields[1].trim();
            String lastName     = fields[2].trim();
            String gender       = fields[3].trim();
            String dob          = fields[4].trim();
            String address      = fields[5].trim();
            String phone        = fields[6].trim();
            List<String> allergies = parseAllergies(fields[7].trim());
            boolean eligible    = Boolean.parseBoolean(fields[8].trim());
            String reason       = fields[9].trim();
            patients.add(new Patient(id, firstName, lastName, gender, dob,
                    address, phone, allergies, eligible, reason));
        }
        return patients;
    }

    /**
     * Reads doctors.csv and returns a list of Doctor objects.
     */
    public List<Doctor> readDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        for (String[] fields : parseFile("doctors.csv")) {
            if (fields.length < 5) continue;
            doctors.add(new Doctor(
                    fields[0].trim(), fields[1].trim(), fields[2].trim(),
                    "N/A", "N/A",
                    fields[3].trim(), fields[4].trim()
            ));
        }
        return doctors;
    }

    /**
     * Reads medications.csv and returns a list of Medication objects.
     */
    public List<Medication> readMedications() {
        List<Medication> medications = new ArrayList<>();
        for (String[] fields : parseFile("medications.csv")) {
            if (fields.length < 5) continue;
            medications.add(new Medication(
                    fields[0].trim(), fields[1].trim(),
                    fields[2].trim(), fields[3].trim(), fields[4].trim()
            ));
        }
        return medications;
    }

    /**
     * Reads pharmacies.csv and returns a list of Pharmacy objects.
     */
    public List<Pharmacy> readPharmacies() {
        List<Pharmacy> pharmacies = new ArrayList<>();
        for (String[] fields : parseFile("pharmacies.csv")) {
            if (fields.length < 4) continue;
            pharmacies.add(new Pharmacy(
                    fields[0].trim(), fields[1].trim(),
                    fields[2].trim(), fields[3].trim()
            ));
        }
        return pharmacies;
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    /**
     * Opens a CSV file, skips the header row, and returns parsed field arrays.
     */
    private List<String[]> parseFile(String fileName) {
        List<String[]> rows = new ArrayList<>();
        Path path = Paths.get(dataDirectory, fileName);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                line = line.trim();
                if (line.isEmpty()) continue;
                rows.add(line.split(",", -1));
            }
        } catch (IOException e) {
            System.err.println("  [CSVManager] Error reading " + fileName + ": " + e.getMessage());
        }
        return rows;
    }

    /**
     * Parses a pipe-separated allergy string into a list.
     * Returns an empty list for "None" or blank values.
     */
    private List<String> parseAllergies(String raw) {
        List<String> list = new ArrayList<>();
        if (raw == null || raw.isEmpty() || raw.equalsIgnoreCase("None")) return list;
        for (String a : raw.split("\\|")) {
            String trimmed = a.trim();
            if (!trimmed.isEmpty()) list.add(trimmed);
        }
        return list;
    }
}
