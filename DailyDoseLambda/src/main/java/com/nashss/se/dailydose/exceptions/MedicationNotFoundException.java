package com.nashss.se.dailydose.exceptions;

public class MedicationNotFoundException extends RuntimeException{
    /**
     * Exception to throw when a given customerId is not found
     * in the database.
     */

    private static final long serialVersionUID = 2713189778644905901L;

    /**
     * Exception with no message or cause.
     */
    public MedicationNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public MedicationNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public MedicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public MedicationNotFoundException(Throwable cause) {
        super(cause);
    }
}
