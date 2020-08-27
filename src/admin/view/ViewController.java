package admin.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import admin.AdminController;
import dbUtil.dbConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ViewController implements Initializable {
	
	@FXML
	private TextField id;
	@FXML
	private TextField name;
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
	
	
	private Connection conn;
	
	public void initialize(URL url, ResourceBundle rb) {
		conn = dbConnection.conn;
		
		this.id.setText(Integer.toString(AdminController.selectedPatient.getID()));
		this.name.setText(AdminController.selectedPatient.getFirstName() + AdminController.selectedPatient.getLastName());
		this.gender.setText(AdminController.selectedPatient.getGender());
		this.email.setText(AdminController.selectedPatient.getEmail());
		this.birthday.setValue(AdminController.LOCAL_DATE(AdminController.selectedPatient.getBirthday()));
		this.appDate.setValue(AdminController.LOCAL_DATE(AdminController.selectedPatient.getAppDate()));
		this.info.setText(AdminController.selectedPatient.getInfo());
	}
}
