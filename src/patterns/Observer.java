package patterns;

/**
 * Observer interface for the Observer Pattern.
 * Any class that wants to receive notifications must implement this interface.
 */
public interface Observer {

    /**
     * Called by the subject (NotificationManager) to deliver a notification message.
     *
     * @param message the notification message
     */
    void update(String message);
}
