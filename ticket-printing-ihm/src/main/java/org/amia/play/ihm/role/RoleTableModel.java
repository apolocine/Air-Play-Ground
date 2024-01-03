package org.amia.play.ihm.role;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.Role;

public class RoleTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = new String[]{"ID", "Role Name"};
    private List<Role> roles;

    public RoleTableModel() {
        roles = new ArrayList<>();
    }

    public void setRoles(List<Role> roles) {
        this.roles = new ArrayList<>(roles);
        fireTableDataChanged();
    }

    public Role getRoleAt(int rowIndex) {
        return roles.get(rowIndex);
    }

    public void removeRole(int rowIndex) {
        if(rowIndex >= 0 && rowIndex < roles.size()) {
            roles.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void addRole(Role role) {
        roles.add(role);
        fireTableRowsInserted(roles.size() - 1, roles.size() - 1);
    }

    @Override
    public int getRowCount() {
        return roles.size();
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
        Role role = roles.get(rowIndex);
        switch (columnIndex) {
            case 0: return role.getRoleID();
            case 1: return role.getRoleName();
            default: return null;
        }
    }
}
