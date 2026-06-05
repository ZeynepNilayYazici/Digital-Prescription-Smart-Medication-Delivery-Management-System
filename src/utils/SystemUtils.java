package utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class providing ID generation and age calculation helpers.
 */
public class SystemUtils {

    private static final AtomicInteger prescriptionCounter = new AtomicInteger(1000);
    private static final AtomicInteger deliveryCounter = new AtomicInteger(5000);

    // Prevent instantiation
    private SystemUtils() {}

    /**
     * Generates a unique prescription ID in the format "RX-XXXX".
     *
     * @return a unique prescription ID string
     */
    public static String generatePrescriptionId() {
        return "RX-" + prescriptionCounter.incrementAndGet();
    }

    /**
     * Generates a unique delivery ID in the format "DLV-XXXX".
     *
     * @return a unique delivery ID string
     */
    public static String generateDeliveryId() {
        return "DLV-" + deliveryCounter.incrementAndGet();
    }

    /**
     * Calculates the age of a person given their date of birth string (yyyy-MM-dd).
     *
     * @param dateOfBirth date of birth in "yyyy-MM-dd" format
     * @return the age in full years, or -1 if the date cannot be parsed
     */
    public static int calculateAge(String dateOfBirth) {
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth);
            return Period.between(dob, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            System.err.println("  [SystemUtils] Could not parse date: " + dateOfBirth);
            return -1;
        }
    }
}
