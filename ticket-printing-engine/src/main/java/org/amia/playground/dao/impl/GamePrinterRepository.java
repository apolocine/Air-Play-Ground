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
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePrinter;
import org.amia.playground.dto.Printer;

public class GamePrinterRepository 
//implements CRUDRepository<GamePrinter> 
{
	private final Connection conn;
	private static final Logger LOGGER = Logger.getLogger(GamePrinterRepository.class.getName());

	private final GameRepository gameRepository; // Initialized elsewhere
	private final PrinterRepository printerRepository; // Initialized elsewhere

	public GamePrinterRepository(Connection conn) {
		this.conn = conn;
		gameRepository = new GameRepository(conn);
		printerRepository = new PrinterRepository(conn);
	}

	public void removeGamePrinter(int gameId, int printerId) {
		String sql = "DELETE FROM GamePrinter WHERE GameID = ? AND PrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gameId);
			stmt.setInt(2, printerId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// Log error and perhaps re-throw as a runtime exception
			LOGGER.log(Level.SEVERE, "Log error removeGamePrinter", e);
		}
	}

	public List<Game> getGamesForPrinter(int printerId) {
		List<Game> games = new ArrayList<>();
		String sql = "SELECT g.* FROM Games g INNER JOIN GamePrinter gp ON g.GameID = gp.GameID WHERE gp.PrinterID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, printerId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Game game = new Game();
				game.setGameID(rs.getInt("GameID"));
				game.setGameName(rs.getString("GameName"));
				game.setAgeRestriction(rs.getString("AgeRestriction"));
				// Add other properties as necessary

				games.add(game); // Add the populated game object to the list
			}
		} catch (SQLException e) {
			// Handle exceptions - log or throw as needed
			LOGGER.log(Level.SEVERE, "Log error getGamesForPrinter", e);
		}

		return games; // Return the list of games
	}

	public List<Printer> getPrintersForGame(int gameId) {
		List<Printer> printers = new ArrayList<>();
		String sql = "SELECT p.* FROM Printers p INNER JOIN GamePrinter gp ON p.PrinterID = gp.PrinterID WHERE gp.GameID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gameId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Printer printer = new Printer();
				printer.setPrinterID(rs.getInt("PrinterID"));
				printer.setName(rs.getString("Name"));
				printer.setLocation(rs.getString("Location"));
				printer.setStatus(rs.getString("Status"));
				printer.setDescription(rs.getString("Description"));
				printer.setActive(rs.getBoolean("IsActive"));
				// Add other properties as necessary

				printers.add(printer); // Add the populated printer object to the list
			}
		} catch (SQLException e) {
			// Handle exceptions - log or throw as needed
			LOGGER.log(Level.SEVERE, "Log error getPrintersForGame", e);
		}

		return printers; // Return the list of printers
	}

	public void addGameToPrinter(int gameId, int printerId) {

		if ((ifGameExists(gameId) && ifPrinterExists(printerId))) {

			if (!ifGamePrinterExists(gameId, printerId)) {

				// Proceed with adding the relationship as both entities exist
				String sql = "INSERT INTO GamePrinter (GameID, PrinterID) VALUES (?, ?)";
				try (PreparedStatement stmt = conn.prepareStatement(sql)) {
					stmt.setInt(1, gameId);
					stmt.setInt(2, printerId);
					int affectedRows = stmt.executeUpdate();
					if (affectedRows == 0) {
						// Handle the case where no rows were updated as needed
						LOGGER.log(Level.SEVERE, "Error adding Printer to Game");
					}
				} catch (SQLException e) {
					// Handle other SQL exceptions here
					LOGGER.log(Level.SEVERE, "Error adding Printer to Game", e);
				}
			} else {
				LOGGER.log(Level.INFO, "The  Game already has this Printer.");
			}

		} else {

			LOGGER.log(Level.INFO, "The   Game or Printer not Exists.");
		}
	}

	private boolean ifGamePrinterExists(int gameId, int printerId) {
		String sql = "SELECT COUNT(*) FROM GamePrinter WHERE GameID = ? AND PrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gameId);
			stmt.setInt(2, printerId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // True if count is greater than 0, meaning the association exists
			} else {
				LOGGER.log(Level.INFO, "The  Game not on this Printer.");
			}
		} catch (SQLException e) {
			// Log error and perhaps re-throw as a runtime exception
			LOGGER.log(Level.SEVERE, "The  Game already or not has this Printe.");
		}
		return false;
	}

	private boolean ifGameExists(int gameId) {
		String sql = "SELECT COUNT(*) FROM Games WHERE GameID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gameId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // True if count is greater than 0, meaning the game exists
			} else {
				LOGGER.log(Level.INFO, "The   Game not Exists.");
			}
		} catch (SQLException e) {
			// Log error and perhaps re-throw as a runtime exception
			LOGGER.log(Level.SEVERE, "The  ifGameExists error .");
		}
		return false;
	}

	private boolean ifPrinterExists(int printerId) {
		String sql = "SELECT COUNT(*) FROM Printers WHERE PrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, printerId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // True if count is greater than 0, meaning the printer exists
			} else {
				LOGGER.log(Level.INFO, "The   Printer not Exists.");
			}
		} catch (SQLException e) {
			// Log error and perhaps re-throw as a runtime exception
			LOGGER.log(Level.SEVERE, "The  ifPrinterExists error .");
		}
		return false;
	}

 
	
//    @Override
    public GamePrinter create(GamePrinter gamePrinter) {
        String sql = "INSERT INTO GamePrinters (GameID, PrinterID, Name, GameImage) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gamePrinter.getGame().getGameID());
            stmt.setInt(2, gamePrinter.getPrinter().getPrinterID());
            stmt.setString(3, gamePrinter.getName());
            stmt.setBytes(4, gamePrinter.getGameImage());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                LOGGER.info("GamePrinter created successfully.");
                return gamePrinter;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating GamePrinter: " + e.getMessage(), e);
        }
        LOGGER.info("GamePrinter Not created.");
        
        return null;
    }

//    @Override
    public List<GamePrinter> readAll() {
        List<GamePrinter> gamePrinters = new ArrayList<>();
        String sql = "SELECT * FROM GamePrinters";
        try (Statement stmt = conn.createStatement(); ResultSet rs  = stmt.executeQuery(sql)) {
        	
        	
//           if (rs_.next()) {
//                GamePrinter gamePrinter = new GamePrinter(); // Assume you have a constructor or setters
//                // Set gamePrinter fields from result set
//                
//                
//                gamePrinters.add(gamePrinter);
//            }
            
        	// Assume you have executed a query and have a ResultSet rs
          // ResultSet is typically obtained from executing a query on a Statement or PreparedStatement
        	    
        		
        		 while (rs.next()) {  // If there's at least one result
        	        GamePrinter gamePrinter = new GamePrinter();

        	        // Directly setting attributes from the ResultSet
        	        gamePrinter.setName(rs.getString("Name")); // Assuming there's a "Name" column in your result set
        	        byte[] imageBytes = rs.getBytes("GameImage"); // Assuming there's a "GameImage" column
        	        gamePrinter.setGameImage(imageBytes); // Assuming setGameImage expects a byte array

        	        // Handling associated Game and Printer objects
        	        // This is simplistic. Normally, you might look up the full Game and Printer objects based on these IDs
        	        int gameId = rs.getInt("GameID");
        	        int printerId = rs.getInt("PrinterID");

        	        Game game = fetchGame(  gameId); // Assuming you can construct a Game like this, or fetch from a repository
        	       // game.setGameID(gameId); // Setting ID, or more complex fetching logic might be involved
        	         
        	        Printer printer = fetchPrinter(printerId); // Assuming you can construct a Printer like this, or fetch from a repository
        	       // printer.setPrinterID(printerId); // Setting ID, or more complex fetching logic might be involved

        	        gamePrinter.setGame(game); // Linking the game to the gamePrinter
        	        gamePrinter.setPrinter(printer); // Linking the printer to the gamePrinter

        	        // Now, gamePrinter is populated with the data from the ResultSet row
        	        // Do something with gamePrinter, like adding it to a list or returning it from a method
        	        
        	        gamePrinters.add(gamePrinter);
        	    }
        	 
            
            
            LOGGER.info("All GamePrinters fetched.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching GamePrinters: " + e.getMessage(), e);
        }
        
        
        return gamePrinters;
    }

 
    
    
    
 
    public boolean delete(Game game, Printer printer) {
        String sql = "DELETE FROM GamePrinters WHERE GameID = ? AND PrinterID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, game.getGameID());
            stmt.setInt(2, printer.getPrinterID());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                LOGGER.info("GamePrinter deleted successfully.");
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting GamePrinter: " + e.getMessage(), e);
        }
        return false;
    }
 
	protected Game fetchGame(int gameId) {
		return gameRepository.read(gameId); // Implement this in your GameRepository
	}

	protected Printer fetchPrinter(int printerId) {
		return printerRepository.read(printerId); // Implement this in your PrinterRepository
	}

	 
	 
	public GamePrinter read(int gameId, int printerId) {
	    String sql = "SELECT * FROM GamePrinters WHERE GameID = ? AND PrinterID = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, gameId);
	        stmt.setInt(2, printerId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                GamePrinter gamePrinter = new GamePrinter();
	                // Assuming the GamePrinter class has setters or an appropriate constructor
	                gamePrinter.setName(rs.getString("Name")); // Example for setting the name
	                gamePrinter.setGameImage(rs.getBytes("GameImage")); // Example for setting the image

	                // For associated Game and Printer objects, you'd typically have their IDs and possibly fetch them completely from their respective repositories.
	                // Here's a simplistic approach where we're just creating stub objects with IDs set
	                Game game = new Game(); // Assuming a Game class exists with a setter for ID
	                game.setGameID(gameId);
	                gamePrinter.setGame(game);

	                Printer printer = new Printer(); // Assuming a Printer class exists with a setter for ID
	                printer.setPrinterID(printerId);
	                gamePrinter.setPrinter(printer);

	                LOGGER.info("GamePrinter fetched successfully.");
	                return gamePrinter;
	            }
	        }
	    } catch (SQLException e) {
	        LOGGER.log(Level.SEVERE, "Error reading GamePrinter: " + e.getMessage(), e);
	    }
	    return null;
	}


//	    @Override
	    public GamePrinter update(GamePrinter gamePrinter) {
	        String sql = "UPDATE GamePrinters SET Name = ?, GameImage = ? WHERE GameID = ? AND PrinterID = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, gamePrinter.getName());
	            stmt.setBytes(2, gamePrinter.getGameImage());
	            stmt.setInt(3, gamePrinter.getGame().getGameID());
	            stmt.setInt(4, gamePrinter.getPrinter().getPrinterID());

	            int affectedRows = stmt.executeUpdate();
	            if (affectedRows > 0) {
	                LOGGER.info("GamePrinter updated successfully.");
	                return gamePrinter;
	            }
	        } catch (SQLException e) {
	            LOGGER.log(Level.SEVERE, "Error updating GamePrinter: " + e.getMessage(), e);
	        }
	        return null;
	    }

 
}
