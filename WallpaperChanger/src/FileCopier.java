import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

/**
 * This is a class that can be used to copy files from a specified destination into a target destination with a target name
 */
public class FileCopier {
	
	/**
	 * The output directory to the folder the image will be copied to
	 */
	private String outputLocation;
	
	/**
	 * Creates an instance of the FileCopier class with a specified output Location
	 * @param outputLocation The directory you want the copy of the image to be saved to
	 */
	public FileCopier(String outputLocation) {
		this.outputLocation = outputLocation;
	}
	
	/**
	 * returns the output directory
	 * @return output directory
	 */
	public String getOutputLocation() {
		return outputLocation;
	}

	/**
	 * Sets the output location of the image copy
	 * @param outputLocation New output location of the image copy
	 */
	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
	}
	
	/**
	 * Duplicates the file from the input directory to the output directory under a new name
	 * @param inputLocation Input directory where the image can be found
	 * @param outputName Name of the copied file
	 * @throws IOException if the input directory does not exist
	 */
	public void duplicateImage(String inputLocation, String outputName) throws IOException {
		FileInputStream inp = null;
		FileOutputStream oup = null;
		inp = new FileInputStream(inputLocation);
		oup = new FileOutputStream(this.outputLocation + "\\" + outputName + ".jpg");
		
		BufferedInputStream inputImage = new BufferedInputStream(inp);
		BufferedOutputStream outputImage = new BufferedOutputStream(oup);
		
		int outputByte = 0;
		while(outputByte != -1) {
			outputByte =  inputImage.read();
			outputImage.write(outputByte);
		}
		
		inputImage.close();
		outputImage.close();
		
		try {
			addImageToDatabase(outputName);
		} catch (SQLException e) {
			System.out.println("Databaase Problem");
		}
	}
	
	/**
	 * This method adds the name of the image to the image database
	 * @param name The name of the method to be added to the database
	 * @throws SQLException if the database or table doess not exist
	 */
	public void addImageToDatabase(String name) throws SQLException {
		Connection connection = DatabaseConnector.getConnection();
		Statement stmt = connection.createStatement();
		stmt.execute("INSERT INTO images (ImageName) VALUES (\""+ name + "\")");
	}
	
	/**
	 * This method finds the ID of a wallpaper in the database given the name. Returns -1 if no wallpaper could be found with the given name.
	 * @param imageName Name of wallpaper
	 * @return ID of wallpaper in the database. Returns -1 if no wallpaper exists with that name
	 */
	public int GetWallpaperID(String imageName) {
		//Creates reference to the connection to the SQL Database
		Connection connection = DatabaseConnector.getConnection();
		
		try {
			//Initializes a statement that will be used to run a query in the database
			Statement stmt = connection.createStatement();
			
			//Queries the database and returns a data set containing the image id for of the wallpaper with the name
			String query = String.format("SELECT ImageID FROM images WHERE ImageName = '%s'", imageName);
			ResultSet results = stmt.executeQuery(query);
			
			//returns the wallpaper ID
			while(results.next()) return results.getInt("ImageID");
		} catch (SQLException e) {
			System.out.println("Database connection could not be established");
		}
		//Returns -1 if no wallpaper could be found with that name
		return -1;
	}
}
