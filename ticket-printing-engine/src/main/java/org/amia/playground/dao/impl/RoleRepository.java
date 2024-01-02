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

public class RoleRepository implements CRUDRepository<Role> {

	private final Connection conn;
	private static final Logger LOGGER = Logger.getLogger(RoleRepository.class.getName());

	public RoleRepository(Connection conn) {
		this.conn = conn;
	}

	public boolean roleExists(String roleName) {
		String sql = "SELECT COUNT(*) FROM Roles WHERE RoleName = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, roleName);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return count > 0;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error checking if role exists", e);
		}
		return false;
	}

	public Role readRoleByName(String roleName) {
		String sql = "SELECT * FROM Roles WHERE RoleName = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, roleName);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Role role = new Role();
				role.setRoleID(rs.getInt("RoleID"));
				role.setRoleName(rs.getString("RoleName"));
				return role;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error reading role", e);
		}
		return null;
	}

//    public Role create(Role role) {
//        if (roleExists(role.getRoleName())) {
//            LOGGER.log(Level.WARNING, "Role with name " + role.getRoleName() + " already exists.");
//            // Handle this according to your application's needs - maybe throw a custom exception or return null
//            return role ;
//        }
//
//        String sql = "INSERT INTO Roles (RoleName) VALUES (?)";
//        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, role.getRoleName());
//
//            int affectedRows = stmt.executeUpdate();
//            if (affectedRows > 0) {
//                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        role.setRoleID(generatedKeys.getInt(1));
//                    }
//                }
//            }
//            return role;
//        } catch (SQLException e) {
//            LOGGER.log(Level.SEVERE, "Error adding role", e);
//            return null;
//        }
//    }

	@Override
	public Role create(Role role) {
		if (roleExists(role.getRoleName())) {
			LOGGER.log(Level.WARNING, "Role with name " + role.getRoleName() + " already exists.");
			// Handle this according to your application's needs - maybe throw a custom
			// exception or return null
			return role;
		}

		String sql = "INSERT INTO Roles (RoleName) VALUES (?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, role.getRoleName());

			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						role.setRoleID(generatedKeys.getInt(1));
					}
				}
			}
			return role;
		} catch (SQLException e) {
			if (e.getSQLState().equals("23000")) {
				// Handle unique constraint violation
				LOGGER.log(Level.WARNING, "Role creation failed: RoleName must be unique.", e);
				throw new IllegalArgumentException("RoleName must be unique.");
			} else {
				LOGGER.log(Level.SEVERE, "Error creating role", e);
				throw new RuntimeException("Database error during role creation", e);
			}
		}
	}

	@Override
	public Role read(int id) {
		String sql = "SELECT * FROM Roles WHERE RoleID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Role role = new Role();
				role.setRoleID(rs.getInt("RoleID"));
				role.setRoleName(rs.getString("RoleName"));
				return role;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error reading role", e);
		}
		return null;
	}

	@Override
	public List<Role> readAll() {
		List<Role> roles = new ArrayList<>();
		String sql = "SELECT * FROM Roles";
		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Role role = new Role();
				role.setRoleID(rs.getInt("RoleID"));
				role.setRoleName(rs.getString("RoleName"));
				roles.add(role);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error reading all roles", e);
		}
		return roles;
	}

	@Override
	public Role update(Role role) {
		String sql = "UPDATE Roles SET RoleName = ? WHERE RoleID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, role.getRoleName());
			stmt.setInt(2, role.getRoleID());

			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				return role;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error updating role", e);
		}
		return null;
	}

	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM Roles WHERE RoleID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);

			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error deleting role", e);
			return false;
		}
	}

	// Methods to add or remove roles from a user

	public void addPermissionToRole(int roleId, int permissionId) {
		// First, check if the role-permission combination already exists
//		if (!rolePermissionExists(roleId, permissionId)) {
		
		
		if (roleExists(roleId) && permissionExists(permissionId)) {
			

			String sql = "INSERT INTO RolePermissions (RoleID, PermissionID) VALUES (?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, roleId);
				stmt.setInt(2, permissionId);

				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
					LOGGER.log(Level.WARNING, "Adding permission to role failed, no rows affected.");
				}
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "Error adding permission to role", e);
			}
		} else {
			LOGGER.log(Level.INFO, "The permission is already assigned to the role.");
		}
	}

	
	
	private boolean roleExists(int roleId) {
	    String sql = "SELECT COUNT(*) FROM Roles WHERE RoleID = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, roleId);

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // Return true if the count is greater than 0
	        }
	    } catch (SQLException e) {
	        LOGGER.log(Level.SEVERE, "Error checking if role exists", e);
	    }
	    return false;
	}

	
	
	private boolean permissionExists(int permissionId) {
	    String sql = "SELECT COUNT(*) FROM Permissions WHERE PermissionID = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, permissionId);

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // Return true if the count is greater than 0
	        }
	    } catch (SQLException e) {
	        LOGGER.log(Level.SEVERE, "Error checking if permission exists", e);
	    }
	    return false;
	}

//	private boolean rolePermissionExists(int roleId, int permissionId) {
//
//		String sql = "SELECT COUNT(*) FROM RolePermissions WHERE RoleID = ? AND PermissionID = ?";
//		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//			stmt.setInt(1, roleId);
//			stmt.setInt(2, permissionId);
//
//			ResultSet rs = stmt.executeQuery();
//			if (rs.next()) {
//				return rs.getInt(1) > 0;
//			}
//		} catch (SQLException e) {
//			LOGGER.log(Level.SEVERE, "Error checking role permission existence", e);
//		}
//		return false;
//	}

	public void removePermissionFromRole(int roleId, int permissionId) {
		String sql = "DELETE FROM RolePermissions WHERE RoleID = ? AND PermissionID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, roleId);
			stmt.setInt(2, permissionId);

			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				LOGGER.log(Level.WARNING, "Removing permission from role failed, no rows affected.");
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error removing permission from role", e);
		}
	}

}
