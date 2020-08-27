package admin.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import admin.AdminController;
import data.PatientData;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewDoctorController implements Initializable {
	
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
	private TextField department;
	@FXML
	private TableView<PatientData> patientTable;
	@FXML
	private TableColumn<PatientData, Integer> idColumn;
	@FXML
	private TableColumn<PatientData, String> fnColumn;
	@FXML
	private TableColumn<PatientData, String> lnColumn;
	@FXML
	private TableColumn<PatientData, String> genderColumn;
	@FXML
	private TableColumn<PatientData, String> emailColumn;
	@FXML
	private TableColumn<PatientData, String> bdayColumn;
	@FXML
	private TableColumn<PatientData, String> appDateColumn;	
	@FXML
	private TableColumn<PatientData, String> infoColumn;
	
	private ResultSet rs;
	private Connection conn;
	
	private ObservableList<PatientData> patientData;
	
	public void initialize(URL url, ResourceBundle rb) {
		rs = null;
		try {
			conn = dbConnection.conn;
			
			this.patientData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(AdminController.sqlLoadPatients);
			
			while (rs.next()) {
				String[] docsPatsArr = rs.getString(9).split(",");
				for (String patID : docsPatsArr) {
					if (Integer.valueOf(patID) == AdminController.selectedDoctor.getID()) {
						this.patientData.add(new PatientData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getInt(9)));
					}
				}
			}
			
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		this.id.setText(Integer.toString(AdminController.selectedDoctor.getID()));
		this.name.setText(AdminController.selectedDoctor.getFirstName() + AdminController.selectedDoctor.getLastName());
		this.gender.setText(AdminController.selectedDoctor.getGender());
		this.email.setText(AdminController.selectedDoctor.getEmail());
		this.birthday.setValue(AdminController.LOCAL_DATE(AdminController.selectedDoctor.getBirthday()));
		this.department.setText(AdminController.selectedDoctor.getDepartment());
		
		this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, Integer>("ID"));
		this.fnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
		this.lnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("lastName"));
		this.genderColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("gender"));
		this.emailColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("email"));
		this.bdayColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("birthday"));
		this.appDateColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("appDate"));
		this.infoColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("info"));
		this.patientTable.setItems(null);
		this.patientTable.setItems(patientData);
	}
}
