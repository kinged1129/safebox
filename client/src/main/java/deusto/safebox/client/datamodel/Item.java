package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.DateTimeProperty;
import deusto.safebox.client.datamodel.property.StringProperty;
import deusto.safebox.client.locale.Message;
import deusto.safebox.client.security.ClientSecurity;
import deusto.safebox.common.AbstractItem;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

abstract class Item extends AbstractItem {

    private Folder folder;
    final StringProperty title;
    final DateTimeProperty created;
    final DateTimeProperty lastModified;

    /**
     * Constructs an {@code Item} with the specified properties.
     *
     * @param id the item id
     * @param type the item type
     * @param title the title of the item
     * @param folder the parent folder of the item
     * @param created the item creation timestamp
     * @param lastModified the timestamp when item was last modified
     */
    Item(UUID id, ItemType type, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified) {
        super(id, type);
        this.folder = folder;
        this.title = new StringProperty(Message.TITLE.get(), 50, title);
        this.created = new DateTimeProperty(Message.CREATED.get(), created);
        this.lastModified = new DateTimeProperty(Message.LAST_MODIFIED.get(), lastModified);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    /** Returns the parent folder of this item. */
    public Folder getFolder() {
        return folder;
    }

    /** Sets the parent folder of this item. */
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    @Override
    public LocalDateTime getCreated() {
        return created.get();
    }

    @Override
    public LocalDateTime getLastModified() {
        return lastModified.get();
    }

    public void setLastModified(LocalDateTime dateTime) {
        lastModified.set(dateTime);
    }

    /**
     * Returns a {@link JsonObject} containing the properties that are specific to an item type.
     *
     * @return a {@link JsonObject} with the data
     */
    abstract JsonObject getCustomData();

    @Override
    protected String getEncryptedData() {
        JsonObject root = getCustomData();
        root.addProperty("title", title.get());
        String folderId = folder == null ? Folder.NO_PARENT_FOLDER_ID : folder.getId().toString();
        root.addProperty("folder", folderId);
        try {
            return ClientSecurity.encrypt(root.toString()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Item encryption execution error.", e);
        }
    }
}
