package admin.edit;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import dbUtil.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import admin.AdminController;

import data.PatientData;
import data.DoctorData;

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
	private TextArea info;
	
	@FXML
	private Button clearButton;
	@FXML
	private Button submitButton;
	
	private dbConnection dc;
	private String sqlSave = "UPDATE patients SET id = ?, first_name = ?, last_name = ?, gender = ?, email = ?, birthday = ?, appointment_date = ?, info = ? WHERE id = ?";
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
		
		this.birthday.setPromptText("Birthday");
		this.appDate.setPromptText("App. Date");
		
		
		this.id.setText(AdminController.selectedPatient.getID());
		this.firstName.setText(AdminController.selectedPatient.getFirstName());
		this.lastName.setText(AdminController.selectedPatient.getLastName());
		this.gender.setText(AdminController.selectedPatient.getGender());
		this.email.setText(AdminController.selectedPatient.getEmail());
		this.birthday.setValue(AdminController.LOCAL_DATE(AdminController.selectedPatient.getBirthday()));
		this.appDate.setValue(AdminController.LOCAL_DATE(AdminController.selectedPatient.getAppDate()));
		this.info.setText(AdminController.selectedPatient.getInfo());
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
		this.info.setText(null);
	}
	
	@FXML
	private void submitEntry(ActionEvent event) throws SQLException {
		boolean entryNotNull = checkNull();
		if (entryNotNull) {
			Connection conn = dbConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlSave);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			
			stmt.setString(1, this.id.getText());
			stmt.setString(2, this.firstName.getText());
			stmt.setString(3, this.lastName.getText());
			stmt.setString(4, this.gender.getText());
			stmt.setString(5, this.email.getText());
			LocalDate bday = this.birthday.getValue();
			if (bday != null) {
			    stmt.setString(6, formatter.format(bday));
			}
			LocalDate aday = this.appDate.getValue(); 
			if (aday != null) {
				stmt.setString(7, formatter.format(aday));
			}
			stmt.setString(8, this.info.getText());
			stmt.setString(9, AdminController.selectedPatient.getID());
			
			stmt.execute();
			conn.close();
		} else {
			System.out.println("Entry is missing values");
		}
	}
	
	// Returns whether or not all fields have been filled out
	private boolean checkNull() {
		return ((this.id.getText()!=null) && (this.firstName.getText()!=null) && (this.lastName.getText()!=null)
				&& (this.gender.getText()!=null) && (this.email.getText()!=null) && (this.birthday.getValue()!=null)
				&& (this.birthday.getValue()!=null) && (this.info.getText()!=null));
	}
}
