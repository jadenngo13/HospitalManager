package admin.edit;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import admin.AdminController;
import data.DoctorData;
import data.PatientData;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditDoctorPatientsController implements Initializable {
	
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
	private TableView<PatientData> patientTable;
	@FXML
	private TableColumn<PatientData, String> idColumn;
	@FXML
	private TableColumn<PatientData, String> fnColumn;
	@FXML
	private TableColumn<PatientData, String> lnColumn;
	@FXML
	private TableColumn<PatientData, String> genderColumn;
	@FXML
	private TableColumn<PatientData, String> emailColumn;
	@FXML
	private TableColumn<PatientData, String> birthdayColumn;
	@FXML
	private TableColumn<PatientData, String> appDateColumn;
	@FXML
	private TableColumn<PatientData, String> infoColumn;
	
	private ObservableList<PatientData> patientData;
	
	private String sqlLoadPatients = "SELECT * FROM patients";
	
	private dbConnection dc;
	
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
		try {
			loadData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadData() throws SQLException {
		try {
			Connection conn = dbConnection.getConnection();
			ResultSet rs = null;
			this.patientData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(sqlLoadPatients);
			while (rs.getString(9).equals(AdminController.selectedDoctor.getID())) {
				this.patientData.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("ID"));
		this.fnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
		this.lnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("lastName"));
		this.genderColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("gender"));
		this.emailColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("email"));
		this.birthdayColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("birthday"));
		this.appDateColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("appDate"));
		this.infoColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("info"));
		this.patientTable.setItems(null);
		this.patientTable.setItems(patientData);
	}
	
	@FXML
	private void addPatient(ActionEvent event) throws SQLException {
		
	}
	
	@FXML
	private void clearPatient(ActionEvent event) throws SQLException {
		
	}
	
	@FXML
	private void loadPatientData(ActionEvent event) throws SQLException {
		
	}
}
















