package com.example.miniproyecto3.exceptions;

/**
 * Exception thrown when a player attempts to shoot a cell on the opponent's board
 * that has already been targeted.

 * This runtime exception helps prevent duplicate actions and ensures valid game flow.
 * It is typically thrown during the player's turn if they click twice on the same cell.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 2.0
 * @see RuntimeException
 */
public class DoubleShootException extends RuntimeException {

    /**
     * Constructs a new DoubleShootException with no detail message.
     */
    public DoubleShootException() {
        super();
    }

    /**
     * Constructs a new DoubleShootException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception.
     */
    public DoubleShootException(String message) {
        super(message);
    }
}
