package admin.edit;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import admin.AdminController;
import dbUtil.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import loginapp.LoginController;
import loginapp.LoginModel;

public class EditUserController implements Initializable {

	@FXML 
	private TextField user;
	@FXML
	private TextField pass;
	@FXML
	private TextField department;
	@FXML
	private TextField id;
	
	private PreparedStatement stmt;
	private Connection conn;
	
	public void initialize(URL url, ResourceBundle rb) {
		conn = LoginModel.conn;
		stmt = null;
		
		this.user.setText(AdminController.selectedUser.getUser());
		this.pass.setText(AdminController.selectedUser.getPass());
		this.department.setText(AdminController.selectedUser.getDepartment());
		this.id.setText(Integer.toString(AdminController.selectedUser.getID()));
	}
	
	@FXML
	private void clearEntry(ActionEvent event) throws SQLException {
		this.user.setText(null);
		this.pass.setText(null);
		this.department.setText(null);
		this.id.setText(null);
	}
	
	@FXML
	private void saveEntry(ActionEvent event) throws SQLException {
		if (checkNull()) {
			stmt = conn.prepareStatement(AdminController.sqlUpdateUsers);
			stmt.setString(1, this.user.getText().toLowerCase());
			stmt.setString(2, this.pass.getText());
			stmt.setString(3, this.department.getText());
			stmt.setString(4, AdminController.selectedUser.getDepartment());
			stmt.setInt(5, Integer.valueOf(this.id.getText()));
			stmt.execute();
		} else {
			System.out.println("Null values. Cannot save entry.");
			return;
		}
	}
	
	// Returns whether or not all fields have been filled out
	private boolean checkNull() {
		return ((this.user.getText()!=null) && (this.pass.getText()!=null) && (this.department.getText()!=null));
	}
}











