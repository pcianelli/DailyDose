package com.nashss.se.dailydose.exceptions;

public class NotificationNotFoundException extends RuntimeException {
    /**
     * Exception to throw when a given Notification time and customerId is not found
     * in the database.
     */

    private static final long serialVersionUID = -8525024759136636587L;

    /**
     * Exception with no message or cause.
     */
    public NotificationNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public NotificationNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public NotificationNotFoundException(Throwable cause) {
        super(cause);
    }
}
