package dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
 
	private static final String USER = "user";
	private static final String PASS = "pass";
	private static final String CONN = "jdbc:mysql://localhost/login";
	private static final String SQCONN = "jdbc:sqlite:hospitalsystem.sqlite";
	
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection(SQCONN);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
