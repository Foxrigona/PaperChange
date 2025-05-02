import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public static void main(String[] args) {
		try {
			ProfileCreator.MakeProfile("ArcaneCharacters", "Jinx", "Singed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
