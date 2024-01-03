package org.amia.play.ihm.permission;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.Permission;

public class PermissionTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = new String[]{"ID", "Action Text"};
    private List<Permission> permissions;

    public PermissionTableModel() {
        permissions = new ArrayList<>();
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = new ArrayList<>(permissions);  // Create a copy of the list
        fireTableDataChanged();  // Notify the table that the data has changed
    }

    public Permission getPermissionAt(int rowIndex) {
        return permissions.get(rowIndex);
    }

    public void removePermission(int rowIndex) {
        if(rowIndex >= 0 && rowIndex < permissions.size()) {
            permissions.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
        fireTableRowsInserted(permissions.size() - 1, permissions.size() - 1);
    }

    @Override
    public int getRowCount() {
        return permissions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Permission permission = permissions.get(rowIndex);
        switch (columnIndex) {
            case 0: return permission.getPermissionID();
            case 1: return permission.getActionText();
            default: return null;
        }
    }
}
