package loginapp;

import dbUtil.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginModel {
	
	public static int docID;
	public static int patID;
	
	private String sqlQuery = "SELECT * FROM login WHERE username=? and password=? and department=?";
	private String sqlGetUserID = "SELECT id FROM login WHERE username=? and password=? and department=?";
	
	public static Connection conn;
	
	public LoginModel() {
		try {
			if (dbConnection.conn == null) {
				dbConnection.conn = dbConnection.getConnection();
			}
			conn = dbConnection.conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (conn == null) {
			System.exit(1); 
		}
	}
	
	public boolean isDBConnected() {
		return conn != null;
	}
	
	public boolean isLogin(String user, String pass, String option) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			statement = conn.prepareStatement(sqlQuery);
			statement.setString(1, user);
			statement.setString(2, pass);
			statement.setString(3, option);
			
			rs = statement.executeQuery();

			if (rs.next()) {
				PreparedStatement stmt = null;
				ResultSet res = null;
				
				stmt = conn.prepareStatement(sqlGetUserID);
				
				stmt.setString(1, user);
				stmt.setString(2, pass);
				stmt.setString(3, option);
				res = stmt.executeQuery();
				
				if (res.next()) {
					if (option.equals("Doctor"))
							docID = res.getInt("id");
					patID = res.getInt("id");
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




