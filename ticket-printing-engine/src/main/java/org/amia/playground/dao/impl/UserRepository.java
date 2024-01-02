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
import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserRepository implements CRUDRepository<User> {

    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

    public UserRepository(Connection conn) {
        this.conn = conn;
    }

  
    
    
    @Override
    public User create(User user) {
        String sql = "INSERT INTO Users (Name, Password) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Hashing user password
            String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
            
            stmt.setString(1, user.getName());
            stmt.setString(2, hashedPassword);  // Use the hashed password

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserID   (generatedKeys.getInt(1));
                    }
                }
            }
            return user;
        } catch (SQLException e) {
            // Handle exceptions and possibly rethrow as a custom exception or log it
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    @Override
    public User read(int id) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                int userId = rs.getInt("UserID");
                user.setUserID(userId);
                user.setName(rs.getString("Name"));
                user.setPassword(rs.getString("Password"));
                
                // Consider how roles are loaded and associated
                List<Role> roles =  getRolesForUser( userId);
                user.setRoles(roles);
                return user;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading user", e);
        }
        return null;
    }

    @Override
    public List<User> readAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setName(rs.getString("Name"));
                user.setPassword(rs.getString("Password")); // Remember this is hashed!
                users.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading all users", e);
        }
        return users;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE Users SET Name = ?, Password = ? WHERE UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword()); // Ensure this password is hashed!
            stmt.setInt(3, user.getUserID());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return user;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
            return false;
        }
    }
 
    public User findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserID (rs.getInt("UserID")); 
                user.setName(rs.getString("Name"));
                user.setPassword(rs.getString("Password")); // Remember this should be hashed!
                // Load roles or any additional data if necessary
                return user;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by username", e);
        }
        return null;
    }
    public List<Role> getRolesForUser(int userId) {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT r.RoleID, r.RoleName FROM Roles r " +
                     "JOIN UserRoles ur ON r.RoleID = ur.RoleID " +
                     "WHERE ur.UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleName(rs.getString("RoleName"));
                roles.add(role);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting roles for user", e);
        }
        return roles;
    }
 
    
    public void addRoleToUser(int userId, int roleId) {
        // First, check if the user-role combination already exists
//        if (!userRoleExists(userId, roleId)) {
    	if ((userExists(userId) && roleExists(roleId))  && !userRoleExists(userId, roleId)) {
            String sql = "INSERT INTO UserRoles (UserID, RoleID) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, roleId);

                stmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error adding role to user", e);
            }
        } else {
            LOGGER.log(Level.INFO, "The user already has this role.");
        }
    }
    
    
    
    private boolean userExists(int userId) {
        String sql = "SELECT COUNT(*) FROM Users WHERE UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // True if count is greater than 0, meaning user exists
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user exists", e);
        }
        return false; // Default to false if user not found or error occurs
    }
    private boolean roleExists(int roleId) {
        String sql = "SELECT COUNT(*) FROM Roles WHERE RoleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // True if count is greater than 0, meaning role exists
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if role exists", e);
        }
        return false; // Default to false if role not found or error occurs
    }

    private boolean userRoleExists(int userId, int roleId) {
        String sql = "SELECT COUNT(*) FROM UserRoles WHERE UserID = ? AND RoleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking user role existence", e);
        }
        return false;
    }

    
    
    public void removeRoleFromUser(int userId, int roleId) {
        String sql = "DELETE FROM UserRoles WHERE UserID = ? AND RoleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing role from user", e);
        }
    }

	 
}
