package admin.edit;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import loginapp.LoginModel;
import admin.AdminController;

public class EditPatientController implements Initializable {

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
	
	private PreparedStatement stmt;
	
	private Connection conn;

	private String sqlSave = "UPDATE patients SET first_name = ?, last_name = ?, gender = ?, email = ?, birthday = ?, appointment_date = ?, info = ? WHERE id = ?";
	
	public void initialize(URL url, ResourceBundle rb) {
		conn = LoginModel.conn;
		
		this.birthday.setPromptText("Birthday");
		this.appDate.setPromptText("App. Date");
		
		
		this.id.setText(Integer.toString(AdminController.selectedPatient.getID()));
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
			
			// Update Patient
			stmt = conn.prepareStatement(sqlSave);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			
			stmt.setString(1, this.firstName.getText());
			stmt.setString(2, this.lastName.getText());
			stmt.setString(3, this.gender.getText());
			stmt.setString(4, this.email.getText());
			LocalDate bday = this.birthday.getValue();
			if (bday != null) {
			    stmt.setString(5, formatter.format(bday));
			}
			LocalDate aday = this.appDate.getValue(); 
			if (aday != null) {
				stmt.setString(6, formatter.format(aday));
			}
			stmt.setString(7, this.info.getText());
			stmt.setInt(8, AdminController.selectedPatient.getID());
			stmt.execute();
		} else {
			System.out.println("Entry is missing values");
		}
	}
	
	// Returns whether or not all fields have been filled out
	private boolean checkNull() {
		return ((this.firstName.getText()!=null) && (this.lastName.getText()!=null)
				&& (this.gender.getText()!=null) && (this.email.getText()!=null) && (this.birthday.getValue()!=null)
				&& (this.appDate.getValue()!=null) && (this.info.getText()!=null));
	}
}
