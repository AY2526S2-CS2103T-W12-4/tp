package seedu.address.ui;

/**
 * Commands trigger a confirmation.
 */
public interface ConfirmationInterface {
    /**
     * User confirms the message.
     * @param message Message to be displayed.
     * @return true if user confirms, false if user denies.
     */
    boolean confirm(String message);
}
