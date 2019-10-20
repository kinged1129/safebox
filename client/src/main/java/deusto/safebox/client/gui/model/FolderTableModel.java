package deusto.safebox.client.gui.model;

import deusto.safebox.client.datamodel.Item;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.common.util.Constants;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class FolderTableModel extends AbstractTableModel {

    // name, type, created, last modified
    private static final int COLUMN_COUNT = 4;

    private List<LeafItem> items = new ArrayList<>();

    public FolderTableModel() {}

    public void setItems(List<LeafItem> items) {
        this.items = items;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Type";
            case 2:
                return "Created";
            case 3:
                return "Last modified";
            default:
                return super.getColumnName(column);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Item item = items.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getItemName();
            case 1:
                return item.getItemType().getName();
            case 2:
                return Constants.DATE_TIME_FORMATTER.format(item.getCreated());
            case 3:
                return Constants.DATE_TIME_FORMATTER.format(item.getLastModified());
            default:
                return "";
        }
    }
}
