package io.chunkworld.client.pong.exceptions;

import org.jbox2d.dynamics.contacts.Contact;

/**
 * Thrown when the userdata is incorrect on a contact event
 */
public class InvalidContactException extends Exception {
    public InvalidContactException() {
        super("Attempted to grab entity ref but found invalid object data");
    }
}
