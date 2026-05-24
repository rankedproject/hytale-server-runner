package wtf.ranked.hytale.server.runner.registrar;

import org.jspecify.annotations.NullMarked;

/**
 * Generic interface for registering plugin components.
 *
 * @param <C> the base type of the component being registered
 */
@NullMarked
public interface Registrar<C> {

    /**
     * Registers a component with a unique identifier.
     *
     * @param identifier unique name or ID for the component
     * @param value the class implementation to register
     */
    void register(String identifier, Class<? extends C> value);
}
