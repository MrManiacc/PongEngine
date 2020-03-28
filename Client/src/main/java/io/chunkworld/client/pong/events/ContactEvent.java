package io.chunkworld.client.pong.events;

import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * A generic contact event posted by the pong physics system
 */
public interface ContactEvent {
    EntityRef getEntityA();

    EntityRef getEntityB();

    Contact getContact();
}
