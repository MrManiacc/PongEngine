package io.chunkworld.client.pong.events;

import com.badlogic.gdx.physics.box2d.Contact;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.client.pong.exceptions.InvalidContactException;
import lombok.Getter;
import lombok.SneakyThrows;

public class ContactEndEvent implements ContactEvent {
    @Getter private EntityRef entityA;
    @Getter private EntityRef entityB;
    @Getter private Contact contact;

    @SneakyThrows
    public ContactEndEvent(Contact contact) {
        this.contact = contact;
        if (!(contact.getFixtureA().getUserData() instanceof EntityRef) || !(contact.getFixtureB().getUserData() instanceof EntityRef))
            throw new InvalidContactException();

        this.entityA = (EntityRef) contact.getFixtureA().getUserData();
        this.entityB = (EntityRef) contact.getFixtureB().getUserData();

    }
}
