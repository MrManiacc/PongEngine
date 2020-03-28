package io.chunkworld.api.core.ecs.entity.system;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.chunkworld.api.core.injection.Injector;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Contains a group of systems
 */
public class EntitySystemManager {
    private final Map<Class<? extends EntitySystem>, EntitySystem> qualifiedSystems = Maps.newConcurrentMap();
    private final List<EntitySystem> systems = Lists.newArrayList();

    /**
     * Adds an entity system
     *
     * @param system the system to add
     */
    public void add(EntitySystem system) {
        if (qualifiedSystems.containsKey(system.getClass())) {
            qualifiedSystems.remove(system.getClass());
            systems.remove(system);
        }
        qualifiedSystems.put(system.getClass(), system);
        systems.add(system);
        Collections.sort(systems);
    }

    /**
     * Removes an entity system
     *
     * @param systemClass the system to remove
     */
    public <T extends EntitySystem> T remove(Class<T> systemClass) {
        if (qualifiedSystems.containsKey(systemClass)) {
            var system = systemClass.cast(qualifiedSystems.remove(systemClass));
            if (systems.remove(system))
                return system;
        }
        return null;
    }

    /**
     * Gets a system by the classified class type
     *
     * @param systemClass
     * @param <T>
     * @return returns the type or null
     */
    public <T extends EntitySystem> T get(Class<T> systemClass) {
        return systemClass.cast(qualifiedSystems.get(systemClass));
    }

    /**
     * Process all of the systems
     */
    public void process() {
        systems.forEach(EntitySystem::update);
    }

    /**
     * Initialize all of the systems
     */
    public void initialize() {
        systems.forEach(system -> {
            if (!system.isInitialized()) {
                Injector.ALL.inject(system);
                system.initialize();
            }
        });
    }
}
