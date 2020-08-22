package admin.edit;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import admin.AdminController;
import data.PatientData;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
	private TableColumn<PatientData, String> idColumn;
	@FXML
	private TableColumn<PatientData, String> fnColumn;
	@FXML
	private TableColumn<PatientData, String> lnColumn;
	@FXML
	private TableColumn<PatientData, String> appDateColumn;
	
	private dbConnection dc;
	
	public PatientData selectedDoctorsPatient;
	
	private ObservableList<PatientData> patientData;
	
	private String sqlLoadPatients = "SELECT * FROM patients";
	private String sqlSave = "UPDATE doctors SET id=?, first_name=?, last_name=?, gender=?, email=?, birthday=?, department=?, patients=? WHERE id=?";
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
		
		try {
			Connection conn = dbConnection.getConnection();
			ResultSet rs = null;
			

			this.patientData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(sqlLoadPatients);
			
			while (rs.next()) {
				this.patientData.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
				String docsPats = patientData.get(patientData.size()-1).getDoctor();
				String[] docsPatsArr = docsPats.split(",");
				for (String patID : docsPatsArr) {
					if (patID.equals(AdminController.selectedDoctor.getID())) {
						this.patientData.get(patientData.size()-1).getSelect().setSelected(true);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		this.id.setText(AdminController.selectedDoctor.getID());
		this.firstName.setText(AdminController.selectedDoctor.getFirstName());
		this.lastName.setText(AdminController.selectedDoctor.getLastName());
		this.gender.setText(AdminController.selectedDoctor.getGender());
		this.email.setText(AdminController.selectedDoctor.getEmail());
		this.birthday.setValue(AdminController.LOCAL_DATE(AdminController.selectedDoctor.getBirthday()));
		this.department.setText(AdminController.selectedDoctor.getDepartment());
		
		this.selectColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("select"));
		this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("ID"));
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
			stmt.setString(7, this.department.getText());
			StringBuilder selectedPats = new StringBuilder();
			for (PatientData patient : patientData) {
				if (patient.getSelect().isSelected()) {
					selectedPats.append(patient.getID() + ",");
				}
			}
			stmt.setString(8, selectedPats.toString());
			stmt.setString(9, AdminController.selectedDoctor.getID());
			stmt.execute();
			
			// Update patients to be added
			stmt = conn.prepareStatement(AdminController.sqlUpdatePatients);
			boolean skip = false;
			for (PatientData patient : patientData) {
				skip = false;
				if (patient.getSelect().isSelected()) {
					String[] docsArr = patient.getDoctor().split(",");
					for (String docID : docsArr) {
						if (docID.equals(AdminController.selectedDoctor.getID())) {
							skip = true;
						}
					}
					if (!skip) {
						stmt.setString(1, AdminController.selectedDoctor.getID());
						stmt.setString(2, patient.getID());
						stmt.execute();
					}
				}
			}
			
			// Update patients to be removed
			stmt = conn.prepareStatement(AdminController.sqlUpdatePatientsDoctor);
			StringBuilder newDoc = null;
			for (PatientData patient : patientData) {
				if (!patient.getSelect().isSelected()) {
					String[] docArr = patient.getDoctor().split(",");
					newDoc = new StringBuilder();
					for (String docID : docArr) {
						if (!docID.equals(AdminController.selectedDoctor.getID())) {
							newDoc.append(docID + ",");
						}
					}
					stmt.setString(1, newDoc.toString());
					stmt.setString(2, patient.getID());
					stmt.execute();
				}
			}
			
			stmt.close();
			conn.close();
		} else {
			System.out.println("Entry is missing values");
		}
	}
	
	// Returns whether or not all fields have been filled out
	private boolean checkNull() {
		return ((this.id.getText()!=null) && (this.firstName.getText()!=null) && (this.lastName.getText()!=null)
				&& (this.gender.getText()!=null) && (this.email.getText()!=null) && (this.birthday.getValue()!=null)
				&& (this.department.getText()!=null));
	}
}











