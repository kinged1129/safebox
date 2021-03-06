package deusto.safebox.client.net;

import deusto.safebox.common.net.packet.ErrorPacket;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/** Event handler for specified error types. */
public class ErrorHandler {

    private static final Map<ErrorPacket.ErrorType, Collection<Runnable>> LISTENERS
            = new EnumMap<>(ErrorPacket.ErrorType.class);

    /**
     * Adds a listener for an error type.
     *
     * @param type the error type.
     * @param action the listener action.
     */
    public static void addListener(ErrorPacket.ErrorType type, Runnable action) {
        LISTENERS.computeIfAbsent(type, e -> new HashSet<>()).add(action);
    }

    /**
     * Fires the listeners of the specified error type.
     *
     * @param type the error type.
     */
    static void fire(ErrorPacket.ErrorType type) {
        Optional.ofNullable(LISTENERS.get(type))
                .ifPresent(listeners -> listeners.forEach(Runnable::run));
    }
}
