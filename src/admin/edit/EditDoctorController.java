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
	private String sqlSave = "UPDATE doctors SET id = ?, first_name = ?, last_name = ?, gender = ?, email = ?, birthday = ?, department = ? WHERE id = ?";
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
		
		try {
			Connection conn = dbConnection.getConnection();
			ResultSet rs = null;
			

			this.patientData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(sqlLoadPatients);
			
			while (rs.next()) {
				if (rs.getString(9).equals(AdminController.selectedDoctor.getID())) {
					this.patientData.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
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
		
		this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("ID"));
		this.fnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
		this.lnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
		this.appDateColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("appDate"));
		this.patientTable.setItems(null);
		this.patientTable.setItems(patientData);
	}
	
	@FXML
	private void editPatients(ActionEvent event) throws SQLException {
		try {
			Stage editStage = new Stage();
			FXMLLoader editLoader = new FXMLLoader();
			Pane editRoot = (Pane)editLoader.load(getClass().getResource("/admin/edit/editDoctorPatientsFXML.fxml").openStream());
		
			Scene editScene = new Scene(editRoot);
			editStage.setScene(editScene);
			editStage.setTitle("Edit Menu");
			editStage.setResizable(false);
			editStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			stmt.setString(8, AdminController.selectedDoctor.getID());
			
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
				&& (this.department.getText()!=null));
	}
}











