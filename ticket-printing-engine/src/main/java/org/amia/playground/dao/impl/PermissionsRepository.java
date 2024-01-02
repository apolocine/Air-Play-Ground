package org.amia.playground.dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.amia.playground.dao.CRUDRepository;
import org.amia.playground.dto.Permission;

public class PermissionsRepository implements CRUDRepository<Permission> {

    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(PermissionsRepository.class.getName());
    public PermissionsRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Permission create(Permission permission) {
    	
    	
        if (permissionExists(permission.getActionText())) {
            LOGGER.log(Level.WARNING, "Permission with name " + permission.getActionText() + " already exists.");
            // Handle this according to your application's needs - maybe throw a custom exception or return null
            return permission ;
        }
        
    	
    	
        String sql = "INSERT INTO Permissions (ActionText) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, permission.getActionText());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated id
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        permission.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return permission;
        } catch (SQLException e) {
            // Handle exceptions, possibly rethrow as a custom exception
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Permission read(int id) {
        String sql = "SELECT * FROM Permissions WHERE PermissionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Permission permission = new Permission();
                permission.setId(rs.getInt("PermissionID"));
                permission.setActionText(rs.getString("ActionText"));
                return permission;
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Permission> readAll() {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT * FROM Permissions";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Permission permission = new Permission();
                permission.setId(rs.getInt("PermissionID"));
                permission.setActionText(rs.getString("ActionText"));
                permissions.add(permission);
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return permissions;
    }

    @Override
    public Permission update(Permission permission) {
        String sql = "UPDATE Permissions SET ActionText = ? WHERE PermissionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, permission.getActionText());
            stmt.setInt(2, permission.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return permission;
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Permissions WHERE PermissionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return false;
    }


    public boolean permissionExists(String actionText) {
        String sql = "SELECT COUNT(*) FROM Permissions WHERE ActionText = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, actionText);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if Permissions exists", e);
        }
        return false;
    }
    

}
