package wtf.ranked.hytale.server.runner.registrar;

import org.jspecify.annotations.NonNull;

/**
 * Generic interface for registering plugin components.
 *
 * @param <C> the base type of the component being registered
 */
public interface Registrar<C> {

    /**
     * Registers a component with a unique identifier.
     *
     * @param identifier unique name or ID for the component
     * @param valueClass the class implementation to register
     */
    void register(@NonNull String identifier, @NonNull Class<? extends C> valueClass);
}
