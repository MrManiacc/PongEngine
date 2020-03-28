package io.chunkworld.api.core.injection.anotations;


import io.chunkworld.api.core.ecs.entity.system.EntitySystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Auto-configures fields or systems pertaining to aspects.
 *
 * <p>fields are configured
 * during{@link EntitySystem#initialize()}</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Single {
    /**
     * The urn of the entity your're attempting to get
     *
     * @return required types
     */
    String value();
}

