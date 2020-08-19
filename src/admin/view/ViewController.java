package admin.view;

import java.net.URL;
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
	
	
	private dbConnection dc;
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
		
		if (AdminController.selectedPatient != null) {
			this.id.setText(AdminController.selectedPatient.getID());
			this.name.setText(AdminController.selectedPatient.getFirstName() + AdminController.selectedPatient.getLastName());
			this.gender.setText(AdminController.selectedPatient.getGender());
			this.email.setText(AdminController.selectedPatient.getEmail());
			this.birthday.setValue(AdminController.LOCAL_DATE(AdminController.selectedPatient.getBirthday()));
			this.appDate.setValue(AdminController.LOCAL_DATE(AdminController.selectedPatient.getAppDate()));
			this.info.setText(AdminController.selectedPatient.getInfo());
		} else {
			this.id.setText(AdminController.selectedDoctor.getID());
			this.name.setText(AdminController.selectedDoctor.getFirstName() + AdminController.selectedPatient.getLastName());
			this.gender.setText(AdminController.selectedDoctor.getGender());
			this.email.setText(AdminController.selectedDoctor.getEmail());
			this.birthday.setValue(AdminController.LOCAL_DATE(AdminController.selectedPatient.getBirthday()));
			//this.department.setText(AdminController.selectedDoctor.getEmail());
			this.info.setText(AdminController.selectedPatient.getInfo());
		}
	}
}
