
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.util.List;

import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dao.impl.PermissionsRepository;
import org.amia.playground.dao.impl.PrinterRepository;
import org.amia.playground.dao.impl.RoleRepository;
import org.amia.playground.dao.impl.UserRepository;
import org.amia.playground.dao.service.AuthorizationService;
import org.amia.playground.dao.service.UserService;
import org.amia.playground.dto.Permission;
import org.amia.playground.dto.Printer;
import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;
import org.hmd.angio.conf.Config;
import org.hmd.angio.install.sgbd.DatabaseInitializerImpl;
import org.hmd.angio.install.sgbd.DatabaseManager;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthenticationTest {
	Connection conn;
	UserService userService;
	AuthorizationService authorizationService;
	PermissionsRepository permissionsRepo  ;
	RoleRepository roleRepo  ;
	UserRepository userRepo  ;
	PrinterRepository printerRepository;
	GameRepository gameRepository;
	
	    @BeforeEach
	void initAll() {
	    	 
//	    	try {
//				DatabaseInitializerImpl.installFromFile();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	    	
		conn = DatabaseManager.getConnection();
		userService = new UserService(conn);
		authorizationService = new AuthorizationService(conn);
		  permissionsRepo = new PermissionsRepository(conn);
		  roleRepo = new RoleRepository(conn);
		  userRepo = new UserRepository(conn);
		   printerRepository = new PrinterRepository(conn);
			 gameRepository  = new GameRepository(conn);
			
	}
	    
	    
	    
@Before
void unInstall() {
	try {
		DatabaseInitializerImpl.unInstallDB(Config.getDatabaseName());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	    @Test
	    void printerTest(){
	    	Printer printer = new Printer("PrinterTest", "PrinterLocationTest", "true" );
	    	
			printerRepository.create(printer);
	    }
	@Test
	void test() {
		 
		String username = "username";
		String password = "password";
		User currentUser = userService.authenticateUser(username, password);
		String edit_permission = "edit_profile";
		// Example of creating a new permission
		Permission newPermission = new Permission(edit_permission/* initialize with values */);
		permissionsRepo.create(newPermission);
		Role role = new Role("Administarteur");
		Role roleXriten = roleRepo.create(role);
		roleRepo.addPermissionToRole(newPermission.getPermissionID(), roleXriten.getRoleID());
		
		
		boolean canEditProfile = authorizationService.checkAccess(currentUser, "edit_profile");

		if (canEditProfile) {
			// Proceed with the action
		} else {
			// Deny access, maybe throw an exception or return an error message
		}
	}

	@Test
	void testCRUD() {

	 	

		String edit_permission = "edit_profile";
		// Example of creating a new permission
		Permission newPermission = new Permission(edit_permission/* initialize with values */);
		permissionsRepo.create(newPermission);
		Role role = new Role("Administarteur");
		Role roleXriten = roleRepo.create(role);
		roleRepo.addPermissionToRole(newPermission.getPermissionID(), roleXriten.getRoleID());
		
		User user = new User("username", "password", roleXriten );
		User userXriten = userRepo.create(user);
		userService.addRoleToUser(user.getUserID(), role.getRoleID());
		
		
		assertEquals(user, userXriten);

	}

	@Test
	void containPermission() {
 
		String edit_permission = "edit_profile";
		// Example of creating a new permission
		Permission newPermission = new Permission(edit_permission/* initialize with values */);
		permissionsRepo.create(newPermission);
		Role role = new Role("Administarteur");
		Role roleXriten = roleRepo.create(role);
		
		roleRepo.addPermissionToRole(newPermission.getPermissionID(), roleXriten.getRoleID());
		String username = "username";
		String password = "password";
		User currentUser = userService.authenticateUser(username, password);
		userService.addRoleToUser(currentUser.getUserID(), roleXriten.getRoleID());
		
		
		List<Role> roles = userRepo.getRolesForUser(currentUser.getUserID()); // list of roles for which to retrieve permissions
		List<String> permissions = authorizationService.getPermissionsForRoles(roles);
  
		assertEquals(permissions.contains(edit_permission), true);
	}

}
