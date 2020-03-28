package io.chunkworld.api.core.ecs.entity.util;

import gnu.trove.iterator.TLongIterator;
import io.chunkworld.api.core.ecs.entity.pool.EntityPool;
import io.chunkworld.api.core.ecs.entity.ref.EntityRef;

import java.util.Iterator;

/**
 * Provides an iterator over EntityRefs, after being given an iterator over entity IDs.
 */
public class EntityIterator implements Iterator<EntityRef> {
    private TLongIterator idIterator;
    private EntityPool pool;

    public EntityIterator(TLongIterator idIterator, EntityPool pool) {
        this.idIterator = idIterator;
        this.pool = pool;
    }

    @Override
    public boolean hasNext() {
        return idIterator.hasNext();
    }

    @Override
    public EntityRef next() {
        return pool.getEntity(idIterator.next());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
