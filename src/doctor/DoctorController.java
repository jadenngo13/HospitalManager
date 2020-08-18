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
	
	private String sqlDelPatient = "DELETE FROM patients WHERE id=?";
	private String sqlLoadPatients = "SELECT * FROM patients";
	
	private dbConnection dc;
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
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
			Connection conn = dbConnection.getConnection();
			ResultSet rs = conn.createStatement().executeQuery(sqlLoadPatients);
			
			this.patientData = FXCollections.observableArrayList();
			while (rs.next()) {
				this.patientData.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
			}
			
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		System.out.println(patientData.size());
		
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
		for (PatientData patient : patientsToDel) {
			try {
				Connection conn = dbConnection.getConnection();
				
				PreparedStatement stmt = conn.prepareStatement(sqlDelPatient);
			    stmt.setString(1, patient.getID());
			    stmt.executeUpdate();
			} catch (SQLException e) {
				System.err.println("Error: " + e);
			}
		}
	}
}
