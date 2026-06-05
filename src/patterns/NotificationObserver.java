package patterns;

/**
 * Concrete Observer implementation.
 * Receives and displays notification messages from the NotificationManager.
 *
 * Examples of messages received:
 *   - "Prescription APPROVED"
 *   - "Delivery COMPLETED"
 *   - "Prescription CANCELLED"
 */
public class NotificationObserver implements Observer {

    private String observerName;

    /**
     * Constructs a NotificationObserver with a given name label.
     *
     * @param observerName label identifying this observer (e.g. "Patient Portal", "Pharmacy System")
     */
    public NotificationObserver(String observerName) {
        this.observerName = observerName;
    }

    /**
     * Receives a notification and prints it to the console.
     *
     * @param message the notification message
     */
    @Override
    public void update(String message) {
        System.out.println("  [NOTIFICATION → " + observerName + "] " + message);
    }

    public String getObserverName() { return observerName; }
}
