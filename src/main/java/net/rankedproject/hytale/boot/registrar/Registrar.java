package net.rankedproject.hytale.boot.registrar;

import org.jetbrains.annotations.NotNull;

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
    void register(@NotNull String identifier, @NotNull Class<? extends C> valueClass);
}