package patient;

import java.net.URL;
import java.util.ResourceBundle;

import data.DoctorData;
import data.PatientData;
import dbUtil.dbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import loginapp.LoginModel;

public class PatientController {

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
	@FXML
	private TableView<DoctorData> doctorTable;
	@FXML
	private TableColumn<DoctorData, String> idColumn;
	@FXML
	private TableColumn<DoctorData, String> fnColumn;
	@FXML
	private TableColumn<DoctorData, String> lnColumn;
	@FXML
	private TableColumn<DoctorData, String> genderColumn;
	@FXML
	private TableColumn<DoctorData, String> emailColumn;
	@FXML
	private TableColumn<DoctorData, String> birthdayColumn;
	@FXML
	private TableColumn<DoctorData, String> departmentColumn;
	
	
	private dbConnection dc;
	
	public void initialize(URL url, ResourceBundle rb) {
		dc = new dbConnection();
		
		System.out.println(LoginModel.patID);
	}
	
	
}
