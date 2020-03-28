package io.chunkworld.api.core.ecs.entity.system;

import io.chunkworld.api.bus.Bus;
import io.chunkworld.api.core.injection.Injector;
import io.chunkworld.api.core.injection.anotations.In;
import io.chunkworld.api.core.assets.urn.ResourceUrn;
import io.chunkworld.api.core.injection.anotations.*;
import io.chunkworld.api.core.ecs.group.Group;
import io.chunkworld.api.core.ecs.group.GroupBuilder;
import io.chunkworld.api.core.ecs.entity.pool.EntityManager;
import io.chunkworld.api.core.injection.InjectionHelper;
import io.chunkworld.api.core.time.EngineTime;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public abstract class EntitySystem implements Comparable<EntitySystem> {
    private int priority;
    private static int nextPriority = 0;

    @In protected EngineTime time;
    @Setter private boolean processing = true;
    @Getter private boolean initialized = false;

    public EntitySystem() {
        this.priority = nextPriority++;
    }

    /**
     * When the entity system is initialized
     */
    public void initialize() {
    }

    /**
     * Internal processing so we can pass the time, which is nice
     *
     * @param time
     */
    protected void process(EngineTime time) {
    }

    /**
     * Post processes the entity system
     */
    protected void postProcess() {
    }

    /**
     * Updates the entity system if it's processing
     */
    public void update() {
        if (processing)
            process(time);
    }

    @Override
    public int compareTo(EntitySystem o) {
        return priority - o.priority;
    }
}
