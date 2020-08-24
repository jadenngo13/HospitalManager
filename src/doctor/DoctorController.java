package doctor;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import admin.AdminController;
import data.DoctorData;
import data.PatientData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import dbUtil.dbConnection;
import loginapp.LoginModel;

public class DoctorController implements Initializable {
 
	@FXML
	private TableView<PatientData> doctorTable;
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
	
	ArrayList<PatientData> patientsToDel = new ArrayList<PatientData>();
	
	private ObservableList<PatientData> patientData;
	
	private DoctorData user;
	
	private PreparedStatement stmt;
	
	private Connection conn;
	
	public void initialize(URL url, ResourceBundle rb) {
		ResultSet rs = null;
		try {
			conn = dbConnection.getConnection();
			rs = null;
			stmt = conn.prepareStatement(AdminController.sqlGetDoctorFromID);
			stmt.setString(1, LoginModel.docID);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.user = new DoctorData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	private void viewPatient(ActionEvent event) throws SQLException {
		AdminController.selectedPatient = doctorTable.getSelectionModel().getSelectedItem();
		if (AdminController.selectedPatient != null) {
			try {
				Stage viewStage = new Stage();
				FXMLLoader viewLoader = new FXMLLoader();
				Pane viewRoot = (Pane)viewLoader.load(getClass().getResource("/admin/view/viewFXML.fxml").openStream());
				
				Scene viewScene = new Scene(viewRoot);
				viewStage.setScene(viewScene);
				viewStage.setTitle("View Menu");
				viewStage.setResizable(false);
				viewStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void editPatient(ActionEvent event) throws SQLException {
		AdminController.selectedPatient = doctorTable.getSelectionModel().getSelectedItem();
		if (AdminController.selectedPatient != null) {
			try {
				Stage editStage = new Stage();
				FXMLLoader editLoader = new FXMLLoader();
				Pane editRoot = (Pane)editLoader.load(getClass().getResource("/admin/edit/editFXML.fxml").openStream());
				
				Scene editScene = new Scene(editRoot);
				editStage.setScene(editScene);
				editStage.setTitle("Edit Menu");
				editStage.setResizable(false);
				editStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void deletePatient(ActionEvent event) throws SQLException {
		AdminController.selectedPatient = doctorTable.getSelectionModel().getSelectedItem();
		patientsToDel.add(AdminController.selectedPatient);
	    doctorTable.getItems().remove(AdminController.selectedPatient);
	    AdminController.selectedPatient = null;
	}
	
	@FXML
	private void refreshPatientData(ActionEvent event) throws SQLException {
		try {
			// Clear patients to be deleted array
			patientsToDel.clear();
			
			ResultSet rs = conn.createStatement().executeQuery(AdminController.sqlLoadPatients);
			
			this.patientData = FXCollections.observableArrayList();
			while (rs.next()) {
				if (rs.getString(9).equals(LoginModel.docID)) {
					this.patientData.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
				}
			}
			rs.close();
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
		this.doctorTable.setItems(null);
		this.doctorTable.setItems(patientData);
	}
	
	@FXML
	private void saveData(ActionEvent event) throws SQLException {
		try {
			for (PatientData patient : patientsToDel) {
				
				// Update doctor
				StringBuilder newPats = new StringBuilder();
				String[] docsPats = user.getPatients().split(",");
				for (String patID : docsPats) {
					if (!patID.equals(patient.getID())) {
						newPats.append(patID + ",");
					}
				}
				stmt = conn.prepareStatement(AdminController.sqlUpdatePatientsDoctor);
				stmt.setString(1, newPats.toString());
				stmt.setString(2, LoginModel.docID);
				stmt.execute();
				
				// Update the patient
				stmt = conn.prepareStatement(AdminController.sqlUpdateDoctorsPatient1);
				stmt.setString(1, "-1");
				stmt.setString(2, patient.getID());
				stmt.execute();
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
	}
}
