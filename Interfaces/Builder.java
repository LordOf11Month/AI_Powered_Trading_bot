package Interfaces;

/**
 * Generic Builder interface for implementing the Builder pattern
 * @param <T> The type of object this builder creates
 */
public interface Builder<T> {
    /**
     * Builds and returns the final object
     * @return The constructed object of type T
     */
    T build();
    
    /**
     * Validates the builder state before building
     * @return true if the builder state is valid
     */
    default boolean isValid() {
        return true;
    }
    
    /**
     * Resets the builder to its initial state
     * @return The builder instance for method chaining
     */
    default Builder<T> reset() {
        return this;
    }
}

