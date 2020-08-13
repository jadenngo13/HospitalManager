package admin.edit;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dbUtil.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import admin.AdminController;

public class EditController implements Initializable {
	
	@FXML
	private TextField id;
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField gender;
	@FXML
	private TextField email;
	@FXML
	private DatePicker birthday;
	@FXML
	private DatePicker appDate;
	
	@FXML
	private Button clearButton;
	@FXML
	private Button submitButton;
	
	private dbConnection dc;
	private String sqlSave = "UPDATE patients SET id = ?, lastName = ?, firstName = ?, gender = ?, email = ?, birthday = ?, appDate = ?";
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
		this.id.setPromptText(AdminController.selectedPatient.getID());
		this.firstName.setPromptText(AdminController.selectedPatient.getFirstName());
		this.lastName.setPromptText(AdminController.selectedPatient.getLastName());
		this.gender.setPromptText(AdminController.selectedPatient.getGender());
		this.email.setPromptText(AdminController.selectedPatient.getEmail());
		this.birthday.setPromptText(AdminController.selectedPatient.getBirthday());
		this.appDate.setPromptText(AdminController.selectedPatient.getAppDate());
	}
	
	@FXML
	private void clearEntry(ActionEvent event) throws SQLException {
		this.id.setText(null);
		this.firstName.setText(null);
		this.lastName.setText(null);
		this.gender.setText(null);
		this.email.setText(null);
		this.birthday.setValue(null);
		this.appDate.setValue(null);
	}
	
	@FXML 
	private void saveEntry(ActionEvent event) throws SQLException {
		
	}
}
