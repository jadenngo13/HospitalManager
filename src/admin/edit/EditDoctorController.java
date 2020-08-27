package admin.edit;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditDoctorController implements Initializable {

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
	private TextField department;
	@FXML
	private TableView<PatientData> patientTable; // Doctor's patients table
	@FXML
	private TableColumn<PatientData, String> selectColumn;
	@FXML
	private TableColumn<PatientData, Integer> idColumn;
	@FXML
	private TableColumn<PatientData, String> fnColumn;
	@FXML
	private TableColumn<PatientData, String> lnColumn;
	@FXML
	private TableColumn<PatientData, String> appDateColumn;
	
	private ResultSet rs;
	private PreparedStatement stmt;
	private Connection conn;
	
	public PatientData selectedDoctorsPatient;
	
	private ObservableList<DoctorData> doctorData;
	private ObservableList<PatientData> patientData;
	private ObservableList<PatientData> selectedPatients;
	
	public void initialize(URL url, ResourceBundle rb) {
		try {
			conn = dbConnection.conn;
			rs = null;
			
			this.selectedPatients = FXCollections.observableArrayList();
			
			// Load doctor's patients
			this.patientData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(AdminController.sqlLoadPatients);
			
			while (rs.next()) {
				this.patientData.add(new PatientData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getInt(9)));
				String[] docsPatsArr = rs.getString(9).split(",");
				for (String patID : docsPatsArr) {
					if (Integer.valueOf(patID) == AdminController.selectedDoctor.getID()) {
						this.patientData.get(patientData.size()-1).getSelect().setSelected(true);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		this.id.setText(Integer.toString(AdminController.selectedDoctor.getID()));
		this.firstName.setText(AdminController.selectedDoctor.getFirstName());
		this.lastName.setText(AdminController.selectedDoctor.getLastName());
		this.gender.setText(AdminController.selectedDoctor.getGender());
		this.email.setText(AdminController.selectedDoctor.getEmail());
		this.birthday.setValue(AdminController.LOCAL_DATE(AdminController.selectedDoctor.getBirthday()));
		this.department.setText(AdminController.selectedDoctor.getDepartment());
		
		this.selectColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("select"));
		this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, Integer>("ID"));
		this.fnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
		this.lnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
		this.appDateColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("appDate"));
		this.patientTable.setItems(null);
		this.patientTable.setItems(patientData);
	}
	
	@FXML
	private void clearEntry(ActionEvent event) throws SQLException {
		this.id.setText(null);
		this.firstName.setText(null);
		this.lastName.setText(null);
		this.gender.setText(null);
		this.email.setText(null);
		this.birthday.setValue(null);
		this.department.setText(null);
		for (PatientData patients : patientData) {
			patients.getSelect().setSelected(false);
		}
	}
	
	@FXML
	private void submitEntry(ActionEvent event) throws SQLException {
		for (PatientData patient : patientData) {
			if (patient.getSelect().isSelected()) {
				selectedPatients.add(patient);
			}
		}
		
		boolean entryNotNull = checkNull();
		if (entryNotNull) {
			
			// Update doctor
			stmt = conn.prepareStatement(AdminController.sqlSave);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			
			stmt.setString(1, this.firstName.getText());
			stmt.setString(2, this.lastName.getText());
			stmt.setString(3, this.gender.getText());
			stmt.setString(4, this.email.getText());
			LocalDate bday = this.birthday.getValue();
			if (bday != null) {
			    stmt.setString(5, formatter.format(bday));
			}
			stmt.setString(6, this.department.getText());
			StringBuilder selectedPats = new StringBuilder();
			for (PatientData selPatient : selectedPatients) {
				selectedPats.append(selPatient.getID() + ",");
			}
			stmt.setString(7, selectedPats.toString());
			stmt.setInt(8, AdminController.selectedDoctor.getID());
			stmt.execute();
			
			// Update patients to be added
			stmt = conn.prepareStatement(AdminController.sqlUpdatePatients);
			for (PatientData selPatient : selectedPatients) {
				stmt.setInt(1, AdminController.selectedDoctor.getID());
				stmt.setInt(2, selPatient.getID());
				stmt.execute();
			}
			
			// Update patients to be removed
			stmt = conn.prepareStatement(AdminController.sqlUpdateDoctorsPatient);
			for (PatientData patient : patientData) {
				if ((!patient.getSelect().isSelected()) && (patient.getDoctor() == AdminController.selectedDoctor.getID())) {
					stmt.setInt(1, -1);
					stmt.setInt(2, patient.getID());
					stmt.execute();
				}
			}
		} else {
			System.out.println("Entry is missing values");
		}
	}
	
	// Returns whether or not all fields have been filled out
	private boolean checkNull() {
		return ((this.firstName.getText()!=null) && (this.lastName.getText()!=null)
				&& (this.gender.getText()!=null) && (this.email.getText()!=null) && (this.birthday.getValue()!=null)
				&& (this.department.getText()!=null));
	}
}











