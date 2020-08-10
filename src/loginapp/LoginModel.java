package loginapp;

import dbUtil.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
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
		
		try {
			statement = this.connection.prepareStatement(sqlQuery);
			statement.setString(1, user);
			statement.setString(2, pass);
			statement.setString(3, option);
			
			rs = statement.executeQuery();
		
			boolean bool;
			if (rs.next()) {
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




