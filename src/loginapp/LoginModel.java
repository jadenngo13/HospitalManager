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
		
		
		String sqlQuery = "SELECT * FROM login WHERE username=? and password=? and department=?";
		String sqlGetDocID = "SELECT id FROM login WHERE username=? and password=? and department=?";
		
		try {
			statement = this.connection.prepareStatement(sqlQuery);
			statement.setString(1, user);
			statement.setString(2, pass);
			statement.setString(3, option);
			
			rs = statement.executeQuery();

			if (rs.next()) {
				// Get logged in doctor's id
				PreparedStatement stmt = null;
				ResultSet res = null;
				stmt = this.connection.prepareStatement(sqlGetDocID);
				stmt.setString(1, user);
				stmt.setString(2, pass);
				stmt.setString(3, option);
				
				res = stmt.executeQuery();
				if (res.next()) {
					docID = res.getString("id");
				}
				statement.close();
				rs.close();
				stmt.close();
				res.close();
				
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}




