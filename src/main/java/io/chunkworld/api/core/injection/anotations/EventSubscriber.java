package io.chunkworld.api.core.injection.anotations;

import io.chunkworld.api.bus.Bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventSubscriber {
    Bus[] value() default Bus.Logic;
}
