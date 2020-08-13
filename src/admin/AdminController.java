package admin;

import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;

public class AdminController implements Initializable {
	
	// Patient Tab
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
	
	// Admin Tab
	@FXML 
	private Button viewButton;
	@FXML 
	private Button editButton;
	@FXML 
	private Button deleteButton;
	@FXML 
	private Button refreshButton;
	@FXML 
	private Button saveButton;
	@FXML
	private TableView<PatientData> patientTable1;
	@FXML
	private TableColumn<PatientData, String> idColumn1;
	@FXML
	private TableColumn<PatientData, String> fnColumn1;
	@FXML
	private TableColumn<PatientData, String> lnColumn1;
	@FXML
	private TableColumn<PatientData, String> genderColumn1;
	@FXML
	private TableColumn<PatientData, String> emailColumn1;
	@FXML
	private TableColumn<PatientData, String> birthdayColumn1;
	@FXML
	private TableColumn<PatientData, String> appDateColumn1;
	
	
	private dbConnection dc;
	private ObservableList<PatientData> data;
	
	private String sqlLoad = "SELECT * FROM patients";
	private String sqlInsert = "INSERT INTO patients(id, first_name, last_name, gender, email, birthday, appointment_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private String sqlDel = "DELETE FROM patients WHERE id=?";
	
	ArrayList<PatientData> patientsToDel = new ArrayList<PatientData>();
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
	}
	
	/***** Patient Tab *****/
	
	@FXML
	private void loadPatientData(ActionEvent event) throws SQLException {
		loadData(event, 0);
	}
	
	@FXML
	private void loadPatientData1(ActionEvent event) throws SQLException {
		loadData(event, 1);
	}
	
	private void loadData(ActionEvent event, int tableNum) throws SQLException {
		try {
			Connection conn = dbConnection.getConnection();
			this.data = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery(sqlLoad);
			
			while (rs.next()) {
				this.data.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		if (tableNum == 0) {
			this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("ID"));
			this.fnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
			this.lnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("lastName"));
			this.genderColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("gender"));
			this.emailColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("email"));
			this.birthdayColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("birthday"));
			this.appDateColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("appDate"));
			this.patientTable.setItems(null);
			this.patientTable.setItems(data);
		} else {
			this.idColumn1.setCellValueFactory(new PropertyValueFactory<PatientData, String>("ID"));
			this.fnColumn1.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
			this.lnColumn1.setCellValueFactory(new PropertyValueFactory<PatientData, String>("lastName"));
			this.genderColumn1.setCellValueFactory(new PropertyValueFactory<PatientData, String>("gender"));
			this.emailColumn1.setCellValueFactory(new PropertyValueFactory<PatientData, String>("email"));
			this.birthdayColumn1.setCellValueFactory(new PropertyValueFactory<PatientData, String>("birthday"));
			this.appDateColumn1.setCellValueFactory(new PropertyValueFactory<PatientData, String>("appDate"));
			this.patientTable1.setItems(null);
			this.patientTable1.setItems(data);
		}
	}
	 
	@FXML
	private void addPatient(ActionEvent event) {
		try {
			Connection conn = dbConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlInsert);	
			
			stmt.setString(1, this.id.getText());
			stmt.setString(2, this.firstName.getText());
			stmt.setString(3, this.lastName.getText());
			stmt.setString(4, this.gender.getText());
			stmt.setString(5, this.email.getText());
			stmt.setString(6, this.birthday.getEditor().getText());
			stmt.setString(7, this.appDate.getEditor().getText());
			
			stmt.execute();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void clearEntry(ActionEvent event) {
		this.id.setText("");
		this.firstName.setText("");
		this.lastName.setText("");
		this.gender.setText("");
		this.email.setText("");
		this.birthday.setValue(null);
		this.appDate.setValue(null);
	}
	
	/***** Admin  Tab 
	 * @throws SQLException *****/
	
	@FXML
	private void deletePatient(ActionEvent event) throws SQLException {
		PatientData selectedPatient = patientTable1.getSelectionModel().getSelectedItem();
		patientsToDel.add(selectedPatient);
	    patientTable1.getItems().remove(selectedPatient);
	}
	
	@FXML
	private void saveData(ActionEvent event) throws SQLException {
		for (PatientData patient : patientsToDel) {
			try {
				Connection conn = dbConnection.getConnection();
				
				PreparedStatement stmt = conn.prepareStatement(sqlDel);
			    stmt.setString(1, patient.getID());
			    stmt.executeUpdate();
			} catch (SQLException e) {
				System.err.println("Error: " + e);
			}
		}
	}
}





