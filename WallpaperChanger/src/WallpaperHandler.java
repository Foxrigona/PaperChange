import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
  * This class is responsible for handling changes in wallpapers.
  */
public final class WallpaperHandler {
	private WallpaperHandler() {
		
	}
	
	public static void AlternateWallpaper() {
		int activeProfileID = ProfileCreator.getActiveProfileID();
		int activeWallpaperID = ProfileCreator.getActiveWallpaperID();
		
		Connection connection = DatabaseConnector.getConnection();
		
		try {
			Statement statement = connection.createStatement();
			
			String query = String.format("SELECT ImageID1, ImageID2 FROM imgprofiles WHERE ProfileID = %d", activeProfileID);
			ResultSet results = statement.executeQuery(query);
			
			while(results.next()) {
				int imageID1 = results.getInt("ImageID1");
				int imageID2 = results.getInt("ImageID2");
				
				if(imageID1 != activeWallpaperID) {
					ProfileCreator.setActiveProfile(activeProfileID, imageID1);
					WallpaperChanger.ChangeWallpaper(FileCopier.GetWallpaperName(imageID1));
				} else {
					ProfileCreator.setActiveProfile(activeProfileID, imageID2);
					WallpaperChanger.ChangeWallpaper(FileCopier.GetWallpaperName(imageID2));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		WallpaperHandler.AlternateWallpaper();
	}
}
