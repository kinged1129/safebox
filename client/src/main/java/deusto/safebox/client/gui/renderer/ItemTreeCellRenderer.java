package deusto.safebox.client.gui.renderer;

import static deusto.safebox.client.util.IconManager.IconType;

import deusto.safebox.client.datamodel.Item;
import deusto.safebox.common.ItemType;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ItemTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value instanceof ItemType) {
            setText(((ItemType) value).getName());
            // TODO: set different icons for each item type
            setIcon(IconType.FOLDER.getAsIcon());
        } else if (value instanceof Item) {
            setText(((Item) value).getItemName());
            // TODO: customize the icon of each item
            setIcon(IconType.NEW_FILE_16.getAsIcon());
        }

        return this;
    }
}
