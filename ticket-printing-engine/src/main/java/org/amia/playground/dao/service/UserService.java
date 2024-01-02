package org.amia.playground.dao.service;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import at.favre.lib.crypto.bcrypt.BCrypt;

import javax.naming.AuthenticationException;

import org.amia.playground.dao.impl.UserRepository;
import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;

public class UserService {

    private final UserRepository userRepository;
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public UserService(Connection conn) {
        this.userRepository = new UserRepository(conn);
    }

    // Authenticate a user
    public User authenticateUser(String username, String password) {
        try {
            // Retrieve the user by username
            User user = userRepository.findByUsername(username);
            if (user != null && BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified) {
                
                List<Role> roles = userRepository.getRolesForUser(user.getUserID());
                user.setRoles(roles);
                return user; // User is authenticated
                
                
            } else {
                throw new AuthenticationException("Invalid username or password");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Authentication failed", e);
            throw new RuntimeException("Authentication failed", e);
        }
    }
    
    
    // Create a new user
    public User createUser(String name, String password, Role role) {
        try {
            User newUser = new User(name, password, role);
            return userRepository.create(newUser);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
            throw new RuntimeException("Unable to create user", e);
        }
    }

    // Retrieve a user by ID
    public User getUserById(int id) {
        try {
            return userRepository.read(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error reading user", e);
            throw new RuntimeException("Unable to read user", e);
        }
    }

    // Retrieve all users
    public List<User> getAllUsers() {
        try {
            return userRepository.readAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error reading all users", e);
            throw new RuntimeException("Unable to read all users", e);
        }
    }

    // Update a user's details
    public User updateUser(User user) {
        try {
            return userRepository.update(user);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
            throw new RuntimeException("Unable to update user", e);
        }
    }

    // Delete a user by ID
    public boolean deleteUser(int id) {
        try {
            return userRepository.delete(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
            throw new RuntimeException("Unable to delete user", e);
        }
    }
    
    
    
    
    // Methods to add or remove roles from a user
    public void addRoleToUser(int userId, int roleId) {
        userRepository.addRoleToUser(userId, roleId);
    }

    public void removeRoleFromUser(int userId, int roleId) {
        userRepository.removeRoleFromUser(userId, roleId);
    }
    
    
}
