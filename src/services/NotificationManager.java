package services;

import patterns.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages notifications using the Observer Pattern.
 * Acts as the subject: observers register to receive messages,
 * and sendNotification() broadcasts to all registered observers.
 */
public class NotificationManager {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Registers a new observer to receive notifications.
     *
     * @param observer the observer to add
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes a registered observer.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Sends a notification message to all registered observers.
     *
     * @param message the notification message to broadcast
     */
    public void sendNotification(String message) {
        displayNotification(message);
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    /**
     * Prints the raw notification to the console (system-level log).
     *
     * @param message the notification message
     */
    public void displayNotification(String message) {
        System.out.println("  [SYSTEM NOTIFICATION] " + message);
    }
}
