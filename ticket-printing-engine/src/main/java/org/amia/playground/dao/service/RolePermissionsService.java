package org.amia.playground.dao.service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.amia.playground.dto.Role;

public class RolePermissionsService {

    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(RolePermissionsService.class.getName());

    public RolePermissionsService(Connection conn) {
        this.conn = conn;
    }
    
    
    public List<String> getPermissionsForRole(int roleId) {
        List<String> permissions = new ArrayList<>();
        String sql = "SELECT p.ActionText FROM Permissions p " +
                     "INNER JOIN RolePermissions rp ON p.PermissionID = rp.PermissionID " +
                     "WHERE rp.RoleID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String permission = rs.getString("ActionText");
                permissions.add(permission);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting permissions for role", e);
        }
        return permissions;
    }
    
    
    public List<String> getPermissionsForRole(List<Role> roles) {
        List<String> permissions = new ArrayList<>();
        if (roles == null || roles.isEmpty()) {
            return permissions;  // Return empty list if no roles are provided
        }

        // Construct the SQL query with IN clause
        StringBuilder roleIds = new StringBuilder();
        for (int i = 0; i < roles.size(); i++) {
            roleIds.append("?,");
        }
        roleIds.deleteCharAt(roleIds.length() - 1);  // Remove the last comma

        String sql = "SELECT DISTINCT p.ActionText FROM Permissions p " +
                     "INNER JOIN RolePermissions rp ON p.PermissionID = rp.PermissionID " +
                     "WHERE rp.RoleID IN (" + roleIds + ")";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set the role IDs in the query
            int index = 1;
            for (Role role : roles) {
                stmt.setInt(index++, role.getRoleID());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String permission = rs.getString("ActionText");
                permissions.add(permission);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting permissions for roles", e);
        }
        return permissions;
    }
}
