package loginapp;

import dbUtil.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginModel {
	
	public static String docID;
	public int patID;
	
	Connection connection;
	
	public LoginModel() {
		try {
			this.connection = dbConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (this.connection == null) {
			System.exit(1); 
		}
	}
	
	public boolean isDBConnected() {
		return this.connection != null;
	}
	
	public boolean isLogin(String user, String pass, String option) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		
		String sqlQuery = "SELECT * FROM login WHERE username = ? and password = ? and department = ?";
		String sqlGetDOCID = "SELECT id FROM doctors WHERE pass=?";
		
		try {
			statement = this.connection.prepareStatement(sqlQuery);
			statement.setString(1, user);
			statement.setString(2, pass);
			statement.setString(3, option);
			
			rs = statement.executeQuery();
		
			boolean bool;
			if (rs.next()) {
				if (option.equals("Doctor")) {
					statement = this.connection.prepareStatement(sqlGetDOCID);
					statement.setString(1, pass);
					rs = statement.executeQuery();
					docID = rs.getString(1);
				}
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		finally {
			statement.close();
			rs.close();
		}
	}
}




