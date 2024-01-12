package org.amia.play.ihm.userroles;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;
import org.amia.playground.dto.UserRole;

public class UserRolesTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = new String[]{"User ID", "Role ID" };
    private List<UserRole> userRoles;

    public UserRolesTableModel() {
    	userRoles = new ArrayList<>();
    }

    // Update the entire list of users
    public void setUserRole(List<UserRole> usersRoles) {
        this.userRoles = new ArrayList<>(usersRoles);  // Create a copy of the list
        fireTableDataChanged();  // Notify the table that the data has changed
    }

    // Get a user at a specific row
    public UserRole getUserRoleAt(int rowIndex) {
        return userRoles.get(rowIndex);
    }

    // Remove a user from a specific row
    public void removeUserRole(int rowIndex) {
        if(rowIndex >= 0 && rowIndex < userRoles.size()) {
        	userRoles.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    // Add a user to the table model
    public void addUserRole(UserRole userRole) {
    	userRoles.add(userRole);
        fireTableRowsInserted(userRoles.size() - 1, userRoles.size() - 1);
    }

    // Update an existing user's details
    public void updateUserRole(UserRole userRole, int rowIndex) {
    	userRoles.set(rowIndex, userRole);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public int getRowCount() {
        return userRoles.size();
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
    	UserRole user = userRoles.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getUserID();
            case 1: return user.getRoleID(); 
            default: return null;
        }
    }

    // Helper method to convert roles list to a comma-separated string
    private String rolesToString(List<Role> roles) {
        StringBuilder sb = new StringBuilder();
        for(Role role : roles) {
            if(sb.length() > 0) sb.append(", ");
            sb.append(role.getRoleName()); // Assumes Role class has getRoleName()
        }
        return sb.toString();
    }
}
