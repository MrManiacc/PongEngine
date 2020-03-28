package io.chunkworld.api.core.context;


public final class CoreRegistry {
    private static Context context;

    private CoreRegistry() {
    }

    /**
     * Registers an object. These objects will be removed when CoreRegistry.clear() is called (typically when game state changes)
     *
     * @param type   The interface which the system fulfils
     * @param object The system itself
     * @param <T>
     */
    public static <T, U extends T> U put(Class<T> type, U object) {
        if (context == null) {
            return null;
        }
        context.put(type, object);
        return object;
    }

    /**
     * Registers an object. These objects will be removed when CoreRegistry.clear() is called (typically when game state changes)
     *
     * @param object The system itself
     */
    public static void put(Object object) {
        if (context == null) {
            return;
        }
        context.put(object);
    }

    /**
     * Checks to see if the class is present
     *
     * @return returns true if present
     */
    public static boolean has(Class<?> type) {
        return get(type) != null;
    }


    /**
     * Sets the context that powers this class.
     *
     * @param context
     */
    public static void setContext(Context context) {
        if (System.getSecurityManager() != null) {
            System.getSecurityManager().checkPermission(new RuntimePermission("permRegister"));
        }
        CoreRegistry.context = context;
    }

    /**
     * @param type
     * @param <T>
     * @return The system fulfilling the given interface
     */
    public static <T> T get(Class<T> type) {
        if (context == null) {
            return null;
        }

        if (type == Context.class) {
            return type.cast(context);
        }
        return context.get(type);
    }

}
