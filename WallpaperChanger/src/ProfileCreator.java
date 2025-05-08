import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.xml.crypto.Data;

/**
 * This utility class is used to create profiles that link image wallpapers together and allow you to switch between them.
 */
public final class ProfileCreator {
	
	private ProfileCreator() {
		
	}
	/**
	 * Creates an image profile in the SQL database with the given image names
	 * @param imageName1 the first images name
	 * @param imageName2 the second images name
	 * @throws SQLException if the images with specified names cannot be found
	 */
	public static void MakeProfile(String profileName, String imageName1, String imageName2) throws SQLException {
		//Creates connection to database and executes query that gets the IDs of the images of the names inputted
		Connection connection = DatabaseConnector.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet IDFinder = stmt.executeQuery("SELECT ImageID FROM images WHERE ImageName = '"+ imageName1 + "' OR ImageName = '"+ imageName2 + "'");
		
		//The variables that will store the image ID
		int imageID1, imageID2;
		
		//Gets the first images ID
		IDFinder.next();
		imageID1 = IDFinder.getInt("ImageID");
		
		//Gets the second images ID
		IDFinder.next();
		imageID2 = IDFinder.getInt("ImageID");
		
		//Inserts profile with name into the database using the two specified images
		stmt.execute("INSERT INTO imgprofiles (ProfileName, ImageID1, ImageID2) VALUES ('"+ profileName + "', '"+ imageID1 + "', '"+ imageID2 + "')");
	}
	
	/**
	 * Sets the active profile that will persist through all sessions of the program
	 * @param profileID The active wallpaper profile ID
	 * @param wallpaperID The active wallpaper ID
	 */
	public static void setActiveProfile(int profileID, int wallpaperID) {
		//Creates an instance of the properties
		Properties active = new Properties();
		try {
			//Creates a link to the properties file to be outputted to
			FileOutputStream file = new FileOutputStream("activeProfile.properties");
			
			//Sets the active profile and wallpaper ID to the properties file
			active.setProperty("activeProfileID", Integer.toString(profileID));
			active.setProperty("activeWallpaperID", Integer.toString(wallpaperID));
			
			//Actually stores the properties in the property file
			active.store(file, null);
		} catch (IOException e) {
			System.out.println("There has been a problem connecting to the properties file");
			return;
		}
	}
	
	/**
	 * Returns the active profile ID of the profile being used
	 * @return The active profile ID of the most recent profile
	 */
	public static int getActiveProfileID() {
		//Creates property instance in order to read from property file
		Properties active = new Properties();
		try {
			//Creates link to the actual properties file
			FileInputStream stream = new FileInputStream("activeProfile.properties");
			
			//Loads the property file into the properties instance
			active.load(stream);			
			
			//Returns active profile ID
			return Integer.parseInt(active.getProperty("activeProfileID"));
		} catch (IOException e) {
			System.out.println("property file could not be found");
			return -1;
		}
	}
	
	/**
	 * Returns the ID of the wallpaper most recently used
	 * @return The ID of the wallpaper most recently used
	 */
	public static int getActiveWallpaperID() {
		//Creates property instance in order to read from property file
		Properties active = new Properties();
		try {
			//Creates link to the actual properties file
			FileInputStream stream = new FileInputStream("activeProfile.properties");
			
			//Loads the property file into the properties instance
			active.load(stream);			
			
			//Returns active wallpaper ID
			return Integer.parseInt(active.getProperty("activeWallpaperID"));
		} catch (IOException e) {
			System.out.println("property file could not be found");
			return -1;
		}
	}
	
	/**
	 * Return the ID of the profile given the name
	 * @param profileName The name of the profile
	 * @return The ID of the profile. Returns -1 if the profile does not exist.
	 */
	public static int getProfileID(String profileName) {
		//References the connection to the database
		Connection c = DatabaseConnector.getConnection();
		
		try {
			//Creates the statement that will be executed
			Statement request = c.createStatement();
			
			//Queries the database for the ID of the profile with the given name and stores it in a result set
			String query = String.format("SELECT ProfileID FROM imgprofiles WHERE ProfileName = '%s'", profileName);			
			ResultSet result = request.executeQuery(query);	
			
			//Returns the profile ID
			while(result.next()) return result.getInt("ProfileID");
		} catch (SQLException e) {
			System.out.println("Database could not be found");
		}
		//Returns -1 if no profile with that name exists in the database
		return -1;
	}
	
	public static void main(String[] args) {
		System.out.println(ProfileCreator.getProfileID("ArcanCharacters"));
	}
}
