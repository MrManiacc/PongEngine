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
    @In protected EntityManager manager;
    @Setter private boolean processing = true;
    @Getter private boolean initialized = false;

    public EntitySystem() {
        this.priority = nextPriority++;
    }

    /**
     * Injects the entity system
     */
    @SneakyThrows
    public void inject() {
//        InjectionHelper.injectGenerics(this);
        Injector.ALL.inject(this);
//        var cls = getClass();
//        var fields = cls.getDeclaredFields();
//        for (var field : fields) {
//            if (field.getType().equals(Group.class)) {
//                field.setAccessible(true);
//                var builder = new GroupBuilder();
//                if (field.isAnnotationPresent(All.class)) {
//                    var allTypes = field.getDeclaredAnnotationsByType(All.class);
//                    for (var all : allTypes)
//                        builder.all(all.value());
//                }
//                if (field.isAnnotationPresent(One.class)) {
//                    var oneTypes = field.getDeclaredAnnotationsByType(One.class);
//                    for (var one : oneTypes)
//                        builder.one(one.value());
//                }
//
//                if (field.isAnnotationPresent(Exclude.class)) {
//                    var excludeTypes = field.getDeclaredAnnotationsByType(Exclude.class);
//                    for (var oneExclude : excludeTypes)
//                        builder.exclude(oneExclude.value());
//                }
//                field.set(this, builder.build(manager));
//            } else if (field.isAnnotationPresent(Single.class)) {
//                field.setAccessible(true);
//                var singleEntity = field.getDeclaredAnnotation(Single.class);
//                var urn = new ResourceUrn(singleEntity.value());
//                var entity = manager.getSingleEntity(urn);
//                if (entity != null) {
//                    field.set(this, entity);
//                }
//            }
//        }
//        //A helper method to register an event system to the given bus
//        if (cls.isAnnotationPresent(EventSubscriber.class)) {
//            var subscription = cls.getDeclaredAnnotation(EventSubscriber.class);
//            for (Bus bus : subscription.value())
//                bus.register(this);
//        }

        this.initialized = true;
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
