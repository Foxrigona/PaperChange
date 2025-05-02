import java.sql.*;

/**
 * This is a utility class that connects to the MqSQL Database giving the application access the the appropriate tables
 */
public final class DatabaseConnector {
	
	private static Connection connection;
	
	private DatabaseConnector() {}
	
	/**
	 * This method initializes the connection to the image profile database
	 */
	public static void InitializeConnector() {
		if(connection != null) return;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/imageprofiles","root","Foxrigona");
		} catch (ClassNotFoundException e) {
			System.out.println("Database Doesnt Exist");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("CONNECTED");
	}
	
	/**
	 * This method returns the Connection object connected to the database
	 * @return connection object connected to the database
	 */
	public static Connection getConnection() {
		if(connection == null) InitializeConnector();
		return connection;
	}

}
