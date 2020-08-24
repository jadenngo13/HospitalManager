package patient;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import admin.AdminController;
import data.DoctorData;
import data.PatientData;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import loginapp.LoginModel;

public class PatientController implements Initializable {

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
	
	private ObservableList<DoctorData> doctorData;
	private PatientData user;
	
	private PreparedStatement stmt;
	private Connection conn;
	
	public void initialize(URL url, ResourceBundle rb) {
		try {
			conn = dbConnection.getConnection();
			ResultSet rs = null;
			
			stmt = conn.prepareStatement(AdminController.sqlGetPatientFromID);
			stmt.setString(1, LoginModel.patID);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.user = new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));
			}
			
			stmt = conn.prepareStatement(AdminController.sqlGetPatientsDoctor);
			stmt.setString(1, user.getDoctor());
			
			this.doctorData = FXCollections.observableArrayList();
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.doctorData.add(new DoctorData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
			}
			
			rs.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}

		this.id.setText(user.getID());
		this.name.setText(user.getFirstName() + user.getLastName());
		this.gender.setText(user.getGender());
		this.email.setText(user.getEmail());
		this.birthday.setValue(AdminController.LOCAL_DATE(user.getBirthday()));
		this.appDate.setValue(AdminController.LOCAL_DATE(user.getBirthday()));
		this.info.setText(user.getInfo());
		
		this.idColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("ID"));
		this.fnColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("firstName"));
		this.lnColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("lastName"));
		this.genderColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("gender"));
		this.emailColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("email"));
		this.birthdayColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("birthday"));
		this.departmentColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("department"));
		this.doctorTable.setItems(null);
		this.doctorTable.setItems(doctorData);
	}
}
