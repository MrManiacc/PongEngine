package io.chunkworld.client.pong.events;

import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.client.pong.exceptions.InvalidContactException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jbox2d.dynamics.contacts.Contact;

public class ContactBeginEvent implements ContactEvent {
    @Getter private EntityRef entityA;
    @Getter private EntityRef entityB;
    @Getter private Contact contact;

    @SneakyThrows
    public ContactBeginEvent(Contact contact) {
        this.contact = contact;
        if (!(contact.getFixtureA().getUserData() instanceof EntityRef) || !(contact.getFixtureB().getUserData() instanceof EntityRef))
            throw new InvalidContactException();

        this.entityA = (EntityRef) contact.getFixtureA().getUserData();
        this.entityB = (EntityRef) contact.getFixtureB().getUserData();

    }
}
