package deusto.safebox.client.net;

import deusto.safebox.client.util.EventHandler;
import deusto.safebox.client.util.IEventHandler;
import deusto.safebox.common.net.packet.ErrorPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.ReceiveDataPacket;
import deusto.safebox.common.net.packet.SuccessfulRegisterPacket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Handles the received packets.
 * Singleton.
 */
public enum PacketHandler {
    INSTANCE;

    private final Map<Class<? extends Packet>, IEventHandler<? extends Packet>> eventHandlerMap = new HashMap<>();

    PacketHandler() {
        addListener(ErrorPacket.class, this::onError);
        addListener(ReceiveDataPacket.class, this::onReceiveData);
        addListener(SuccessfulRegisterPacket.class, this::onSuccessfulRegister);
        // TODO: add the remaining listeners
    }

    /**
     * Registers a listener of a given packet type.
     *
     * @param classType the class of the packet.
     * @param consumer the event callback.
     * @param <T> the class type of the packet.
     */
    public <T extends Packet> void addListener(Class<T> classType, Consumer<T> consumer) {
        // Type safe.
        @SuppressWarnings("unchecked")
        EventHandler<T> eventHandler = (EventHandler<T>) eventHandlerMap
                .computeIfAbsent(classType, type -> new EventHandler<T>());
        eventHandler.addListener(consumer);
    }

    /**
     * Calls the listeners of a given packet type with the provided packet.
     *
     * @param object the packet.
     * @param <T> the packet type.
     */
    public <T extends Packet> void fire(T object) {
        // Type safe because the key (the class type) always matches the event handler type.
        @SuppressWarnings("unchecked")
        EventHandler<T> eventHandler = (EventHandler<T>) eventHandlerMap.get(object.getClass());
        eventHandler.fire(object);
    }

    private void onError(ErrorPacket packet) {
        ErrorHandler.INSTANCE.fire(packet.getErrorType());
    }

    private void onReceiveData(ReceiveDataPacket packet) {
        // TODO
    }

    private void onSuccessfulRegister(SuccessfulRegisterPacket packet) {
        // TODO: show successful register dialog
    }
}
