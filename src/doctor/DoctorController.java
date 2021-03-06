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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import loginapp.LoginModel;
import sql.SqlQueries;

public class DoctorController implements Initializable {
 
	@FXML
	private TableView<PatientData> doctorTable;
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
	private TableColumn<PatientData, String> birthdayColumn;
	@FXML
	private TableColumn<PatientData, String> appDateColumn;
	@FXML
	private TableColumn<PatientData, String> infoColumn;
	@FXML
	private Button logoutButton;
	
	ArrayList<PatientData> patientsToDel = new ArrayList<PatientData>();
	
	private ObservableList<PatientData> patientData;
	
	private DoctorData user;
	
	private ResultSet rs;
	private PreparedStatement stmt;
	private Connection conn;
	
	public void initialize(URL url, ResourceBundle rb) {
		rs = null;
		stmt = null;
		try {
			if (LoginModel.conn != null) {
				conn = LoginModel.conn;
			}
			
			
			// Load user
			stmt = conn.prepareStatement(SqlQueries.sqlGetDoctorFromID);
			stmt.setInt(1, LoginModel.docID);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.user = new DoctorData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));
			}
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
				Pane editRoot = (Pane)editLoader.load(getClass().getResource("/admin/edit/editPatientFXML.fxml").openStream());
				
				if (editRoot != null) {
					Scene editScene = new Scene(editRoot);
					editStage.setScene(editScene);
					editStage.setTitle("Edit Menu");
					editStage.setResizable(false);
					editStage.show();
				} 
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
			patientsToDel.clear();
			
			rs = conn.createStatement().executeQuery(SqlQueries.sqlLoadPatients);
			
			this.patientData = FXCollections.observableArrayList();
			while (rs.next()) {
				if (rs.getInt(9) == LoginModel.docID) {
					this.patientData.add(new PatientData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getInt(9)));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, Integer>("ID"));
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
					if (Integer.valueOf(patID) != patient.getID()) {
						newPats.append(patID + ",");
					}
				}
				stmt = conn.prepareStatement(SqlQueries.sqlUpdatePatientsDoctor);
				stmt.setString(1, newPats.toString());
				stmt.setInt(2, LoginModel.docID);
				stmt.execute();
				
				// Update patient
				stmt = conn.prepareStatement(SqlQueries.sqlUpdateDoctorsPatient1);
				stmt.setInt(1, -1);
				stmt.setInt(2, patient.getID());
				stmt.execute();
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
	}
	
	@FXML
	private void logout(ActionEvent event) throws SQLException {
		// Closeout current window
		Stage stage = (Stage) logoutButton.getScene().getWindow();
		stage.close();
		
		// Reload login window
		FXMLLoader loader = new FXMLLoader();
		stage = new Stage();
		Pane root = null;
		try {
			root = (Pane)loader.load(getClass().getResource("/loginapp/login.fxml").openStream());
			if (root != null) {
				Scene editScene = new Scene(root);
				stage.setScene(editScene);
				stage.setTitle("Login Menu");
				stage.setResizable(false);
				stage.show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}
