package deusto.safebox.common.net.packet;

import deusto.safebox.common.net.ItemPacketData;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class SaveDataPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 6465366035399352082L;

    private final Collection<ItemPacketData> items;

    public SaveDataPacket(Collection<ItemPacketData> items) {
        this.items = items;
    }

    public Collection<ItemPacketData> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "SaveItemsPacket: " + Arrays.toString(items.toArray());
    }
}