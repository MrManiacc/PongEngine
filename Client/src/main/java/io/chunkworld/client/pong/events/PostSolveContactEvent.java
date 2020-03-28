package io.chunkworld.client.pong.events;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;
import io.chunkworld.client.pong.exceptions.InvalidContactException;
import lombok.Getter;
import lombok.SneakyThrows;

public class PostSolveContactEvent implements ContactEvent {
    @Getter private EntityRef entityA;
    @Getter private EntityRef entityB;
    @Getter private Contact contact;
    @Getter private ContactImpulse impulse;

    @SneakyThrows
    public PostSolveContactEvent(Contact contact, ContactImpulse impulse) {
        this.contact = contact;
        if (!(contact.getFixtureA().getUserData() instanceof EntityRef) || !(contact.getFixtureB().getUserData() instanceof EntityRef))
            throw new InvalidContactException();

        this.entityA = (EntityRef) contact.getFixtureA().getUserData();
        this.entityB = (EntityRef) contact.getFixtureB().getUserData();
        this.impulse = impulse;
    }
}
