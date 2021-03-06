package deusto.safebox.common;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractItem {

    private final UUID id;
    private final ItemType type;

    protected AbstractItem(UUID id, ItemType type) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
    }

    public UUID getId() {
        return id;
    }

    public ItemType getType() {
        return type;
    }

    public abstract LocalDateTime getCreated();

    public abstract LocalDateTime getLastModified();

    /** Returns an encrypted string containing the sensitive data of the item. */
    protected abstract String getEncryptedData();
}
