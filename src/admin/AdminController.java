package admin;

import java.io.IOException;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
	@FXML
	private TableColumn<PatientData, String> infoColumn1;
	
	
	private dbConnection dc;
	private ObservableList<PatientData> data;
	
	private String sqlLoad = "SELECT * FROM patients";
	private String sqlInsert = "INSERT INTO patients(id, first_name, last_name, gender, email, birthday, appointment_date, info) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private String sqlDel = "DELETE FROM patients WHERE id=?";
	
	ArrayList<PatientData> patientsToDel = new ArrayList<PatientData>();
	public static PatientData selectedPatient;
	
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
				this.data.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
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
			this.infoColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("info"));
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
			this.infoColumn1.setCellValueFactory(new PropertyValueFactory<PatientData, String>("info"));
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
			stmt.setString(8, this.info.getText());
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate bday = this.birthday.getValue();
			LocalDate aday = this.appDate.getValue();
			if (bday != null) {
			    stmt.setString(6, formatter.format(bday));
			}
			if (aday != null) {
				stmt.setString(7, formatter.format(aday));
			}
			
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
		this.info.setText("");
	}
	
	/***** Admin  Tab *****/
	
	@FXML
	private void deletePatient(ActionEvent event) throws SQLException {
		selectedPatient = patientTable1.getSelectionModel().getSelectedItem();
		patientsToDel.add(selectedPatient);
	    patientTable1.getItems().remove(selectedPatient);
	    selectedPatient = null;
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
	
	@FXML
	private void editPatient(ActionEvent event) throws SQLException {
		selectedPatient = patientTable1.getSelectionModel().getSelectedItem();
		if (selectedPatient != null) {
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
	private void viewPatient(ActionEvent event) throws SQLException {
		selectedPatient = patientTable1.getSelectionModel().getSelectedItem();
		if (selectedPatient != null) {
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
	
	/***** Functionality Methods *****/
	public static final LocalDate LOCAL_DATE (String dateString){
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	    LocalDate localDate = LocalDate.parse(dateString, formatter);
	    return localDate;
	}
}




//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//LocalDate bday = LocalDate.parse(this.birthday.getPromptText()); 
// LocalDate aday = LocalDate.parse("2018-11-27"); 


