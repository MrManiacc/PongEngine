package io.chunkworld.api.core.injection;

import io.chunkworld.api.core.annotations.In;
import io.chunkworld.api.core.annotations.Share;
import io.chunkworld.api.core.context.Context;
import io.chunkworld.api.core.context.CoreRegistry;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

/**
 * Handles injections into objects
 */
public class InjectionHelper {

    private InjectionHelper() {
    }

    public static void inject(final Object object, Context context) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            for (Field field : ReflectionUtils.getAllFields(object.getClass(), ReflectionUtils.withAnnotation(In.class))) {
                Object value = context.get(field.getType());
                if (value != null) {
                    try {
                        field.setAccessible(true);
                        field.set(object, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        });
    }

    public static void inject(final Object object) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            for (Field field : ReflectionUtils.getAllFields(object.getClass(), ReflectionUtils.withAnnotation(In.class))) {
                Object value = CoreRegistry.get(field.getType());
                if (value != null) {
                    try {
                        field.setAccessible(true);
                        field.set(object, value);
                    } catch (IllegalAccessException e) {
                        System.err.printf("Failed to inject value %s into field %s of %s", value, field, object, e);
                    }
                }
            }

            return null;
        });
    }

    public static <T> void inject(final Object object, final Class<? extends Annotation> annotation, final Map<Class<? extends T>, T> source) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            for (Field field : ReflectionUtils.getAllFields(object.getClass(), ReflectionUtils.withAnnotation(annotation))) {
                Object value = source.get(field.getType());
                if (value != null) {
                    try {
                        field.setAccessible(true);
                        field.set(object, value);
                    } catch (IllegalAccessException e) {
                        //     logger.error("Failed to inject value {} into field {}", value, field, e);
                        e.printStackTrace();
                    }
                } else {
//                    logger.error("Failed to inject into field {}, nothing to inject", field);
                    System.err.println("Failed to inject filed nothing to inject: " + field.getName());
                }
            }

            return null;
        });
    }

    /**
     * Shares a class, an allows it to be injected with {@link In}
     * This must be called after the object has been initialized inside the pre init of subsystems!!
     *
     * @param object the object to share
     */
    @SneakyThrows
    public static void share(Object object) {
        var clazz = object.getClass();
        var clazzAnnotations = clazz.getAnnotations();
        for (var annotation : clazzAnnotations) {
            if (annotation instanceof Share) {
                var share = (Share) annotation;
                registerSharedValue(share, object);
            }
        }

        var fields = clazz.getDeclaredFields();
        for (var field : fields) {
            for (var annotation : field.getAnnotations()) {
                if (annotation instanceof Share) {
                    var share = (Share) annotation;
                    registerSharedValue(share, field.get(object));
                }
            }
        }
    }

    /**
     * Registers a shared value to the core registry
     *
     * @param share the share annotation
     * @param value the shared value
     */
    private static void registerSharedValue(Share share, Object value) {
        for (Class interfaceType : share.value()) {
            CoreRegistry.put(interfaceType, value);
        }
    }

}
