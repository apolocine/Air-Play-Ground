package org.amia.play.ihm.user;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;

public class UserTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = new String[]{"ID", "Name", "Roles"};
    private List<User> users;

    public UserTableModel() {
        users = new ArrayList<>();
    }

    // Update the entire list of users
    public void setUsers(List<User> users) {
        this.users = new ArrayList<>(users);  // Create a copy of the list
        fireTableDataChanged();  // Notify the table that the data has changed
    }

    // Get a user at a specific row
    public User getUserAt(int rowIndex) {
        return users.get(rowIndex);
    }

    // Remove a user from a specific row
    public void removeUser(int rowIndex) {
        if(rowIndex >= 0 && rowIndex < users.size()) {
            users.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    // Add a user to the table model
    public void addUser(User user) {
        users.add(user);
        fireTableRowsInserted(users.size() - 1, users.size() - 1);
    }

    // Update an existing user's details
    public void updateUser(User user, int rowIndex) {
        users.set(rowIndex, user);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public int getRowCount() {
        return users.size();
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
        User user = users.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getUserID();
            case 1: return user.getName();
            case 2: return rolesToString(user.getRoles()); // Convert roles list to String
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
