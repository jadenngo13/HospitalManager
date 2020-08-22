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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;

import data.PatientData;
import data.DoctorData;

public class AdminController implements Initializable {
	
	/***** Patients  Tab *****/
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
	private TableView<PatientData> patientTable; // Main patient table
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
	
	/***** Doctors  Tab *****/
	@FXML 
	private TextField id2;
	@FXML
	private TextField firstName2;
	@FXML
	private TextField lastName2;
	@FXML
	private TextField gender2;
	@FXML
	private TextField email2;
	@FXML
	private DatePicker birthday2;
	@FXML
	private TextField department;
	@FXML 
	private TableView<PatientData> patientsTable1;
	@FXML
	private TableColumn<PatientData, String> selectColumn;
	@FXML
	private TableColumn<PatientData, String> idColumn3;
	@FXML
	private TableColumn<PatientData, String> fnColumn3;
	@FXML
	private TableColumn<PatientData, String> lnColumn3;
	@FXML
	private TableColumn<PatientData, String> genderColumn3;
	@FXML
	private TableView<DoctorData> doctorTable; // Main doctor table
	@FXML
	private TableColumn<DoctorData, String> idColumn2;
	@FXML
	private TableColumn<DoctorData, String> fnColumn2;
	@FXML
	private TableColumn<DoctorData, String> lnColumn2;
	@FXML
	private TableColumn<DoctorData, String> genderColumn2;
	@FXML
	private TableColumn<DoctorData, String> emailColumn2;
	@FXML
	private TableColumn<DoctorData, String> birthdayColumn2;
	@FXML
	private TableColumn<DoctorData, String> departmentColumn;
	@FXML
	private TableColumn<DoctorData, String> patientsColumn;
	
	
	/***** Doctors  Tab *****/
	private dbConnection dc;
	private ObservableList<PatientData> patientData;
	private ObservableList<DoctorData> doctorData;
	
	private String adminTab = "Patients";
	
	private String sqlLoadPatients = "SELECT * FROM patients";
	private String sqlLoadDoctors = "SELECT * FROM doctors";
	private String sqlInsertPatient = "INSERT INTO patients(id, first_name, last_name, gender, email, birthday, appointment_date, info, doctor) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private String sqlInsertDoctor = "INSERT INTO doctors(id, first_name, last_name, gender, email, birthday, department, patients) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	private String sqlDelPatients = "DELETE FROM patients WHERE id=?";
	private String sqlDelDoctors = "DELETE FROM doctors WHERE id=?";
	public static String sqlUpdatePatients = "UPDATE patients SET doctor=(doctor||','||?) WHERE id=?";
	public static String sqlUpdatePatientsDoctor = "UPDATE patients SET doctor=? WHERE id=?";
	
	private ArrayList<PatientData> patientsToDel = new ArrayList<PatientData>();
	private ArrayList<DoctorData> doctorsToDel = new ArrayList<DoctorData>();
	
	public static PatientData selectedPatient;
	public static DoctorData selectedDoctor;
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
	}
	

	@FXML
	private void patientTab(Event event) throws SQLException {
		adminTab = "Patients";
		selectedDoctor = null;
	}
	
	@FXML
	private void doctorTab(Event event) throws SQLException {
		adminTab = "Doctors";
		selectedPatient = null;
	}
	
	@FXML
	private void refreshData(ActionEvent event) throws SQLException {
		try {
			Connection conn = dbConnection.getConnection();
			ResultSet rs = null;
			
			this.patientData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(sqlLoadPatients);
			while (rs.next()) {
				this.patientData.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
			}
		
			if (adminTab.equals("Doctors")) {
				this.doctorData = FXCollections.observableArrayList();
				rs = conn.createStatement().executeQuery(sqlLoadDoctors);
				while (rs.next()) {
					this.doctorData.add(new DoctorData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
				}
			}
			
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		if (adminTab.equals("Patients")) {
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
		} else if (adminTab.equals("Doctors")) {
			this.selectColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("select"));
			this.idColumn3.setCellValueFactory(new PropertyValueFactory<PatientData, String>("ID"));
			this.fnColumn3.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
			this.lnColumn3.setCellValueFactory(new PropertyValueFactory<PatientData, String>("lastName"));
			this.genderColumn3.setCellValueFactory(new PropertyValueFactory<PatientData, String>("gender"));
			this.patientsTable1.setItems(null);
			this.patientsTable1.setItems(patientData);
			
			this.idColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("ID"));
			this.fnColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("firstName"));
			this.lnColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("lastName"));
			this.genderColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("gender"));
			this.emailColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("email"));
			this.birthdayColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("birthday"));
			this.departmentColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("department"));
			this.patientsColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("patients"));
			this.doctorTable.setItems(null);
			this.doctorTable.setItems(doctorData);
		}
	}
	
	@FXML
	private void saveData(ActionEvent event) throws SQLException {
		Connection conn = null;
		if (adminTab.equals("Patients")) {
			for (PatientData patient : patientsToDel) {
				try {
					conn = dbConnection.getConnection();
					
					PreparedStatement stmt = conn.prepareStatement(sqlDelPatients);
				    stmt.setString(1, patient.getID());
				    stmt.executeUpdate();
					stmt.close();
				} catch (SQLException e) {
					System.err.println("Error: " + e);
				}
			}
		} else if (adminTab.equals("Doctors")){
			for (DoctorData doctor : doctorsToDel) {
				try {
					conn = dbConnection.getConnection();
					
					PreparedStatement stmt = conn.prepareStatement(sqlDelDoctors);
				    stmt.setString(1, doctor.getID());
				    stmt.executeUpdate();
					stmt.close();
				} catch (SQLException e) {
					System.err.println("Error: " + e);
				}
			}
		} else {
			return;
		}
	}

	@FXML
	private void addEntry(ActionEvent event) {
		try {
			Connection conn = dbConnection.getConnection();
			PreparedStatement stmt = null;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			
			if (adminTab.equals("Patients")) { //SQL query inserts that differ for patients
				stmt = conn.prepareStatement(sqlInsertPatient);	
				stmt.setString(1, this.id.getText());
				stmt.setString(2, this.firstName.getText());
				stmt.setString(3, this.lastName.getText());
				stmt.setString(4, this.gender.getText());
				stmt.setString(5, this.email.getText());
				LocalDate bday = this.birthday.getValue();
				if (bday != null) {
				    stmt.setString(6, formatter.format(bday));
				}
				LocalDate aday = this.appDate.getValue(); 
				if (aday != null) {
					stmt.setString(7, formatter.format(aday));
				}
				stmt.setString(8, this.info.getText());
				stmt.setString(9, null);
				stmt.execute();
			} else if (adminTab.equals("Doctors")) { // SQL query inserts that differ for doctors
				stmt = conn.prepareStatement(sqlInsertDoctor);
				stmt.setString(1, this.id2.getText());
				stmt.setString(2, this.firstName2.getText());
				stmt.setString(3, this.lastName2.getText());
				stmt.setString(4, this.gender2.getText());
				stmt.setString(5, this.email2.getText());
				LocalDate bday = this.birthday2.getValue();
				if (bday != null) {
				    stmt.setString(6, formatter.format(bday));
				}
				stmt.setString(7, this.department.getText());
				
				StringBuilder pats = new StringBuilder();
				for (PatientData patient : patientData) {
					if (patient.getSelect().isSelected()) {
						pats.append(patient.getID() + ",");
					}
				}
				stmt.setString(8, pats.toString());
				stmt.execute();
				
				// Update patients
				String[] patsArr = pats.toString().split(",");
				for (String id : patsArr) {
					stmt = conn.prepareStatement(sqlUpdatePatients);
					stmt.setString(1, this.id2.getText());
					stmt.setString(2, id);
					stmt.execute();
				}
			} else {
				System.out.println("No entry selected for add.");
				return;
			}
			
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void clearEntry(ActionEvent event) throws SQLException {
		if (adminTab.equals("Patients")) {
			this.id.setText(null);
			this.firstName.setText(null);
			this.lastName.setText(null);
			this.gender.setText(null);
			this.email.setText(null);
			this.birthday.setValue(null);
			this.appDate.setValue(null);
			this.info.setText(null);
		} else if (adminTab.equals("Doctors")){
			this.id2.setText(null);
			this.firstName2.setText(null);
			this.lastName2.setText(null);
			this.gender2.setText(null);
			this.email2.setText(null);
			this.birthday2.setValue(null);
			this.department.setText(null);
			for (PatientData patients : patientData) {
				patients.getSelect().setSelected(false);
			}
		} else {
			System.out.println("No entry selected for clear.");
			return;
		}
	}
	
	@FXML
	private void viewEntry(ActionEvent event) throws SQLException {
		if (adminTab.equals("Patients")) {
			selectedPatient = patientTable.getSelectionModel().getSelectedItem();
		} else if (adminTab.equals("Doctors")){
			selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
		} else {
			System.out.println("No entry selected for view.");
			return;
		}
		
		if (selectedPatient != null || selectedDoctor != null) {
			try {
				Stage viewStage = new Stage();
				FXMLLoader viewLoader = new FXMLLoader();
				
				Pane viewRoot = null;
				if (selectedPatient != null) {
					viewRoot = (Pane)viewLoader.load(getClass().getResource("/admin/view/viewFXML.fxml").openStream());
				} else if (selectedDoctor != null){
					viewRoot = (Pane)viewLoader.load(getClass().getResource("/admin/view/viewDoctorFXML.fxml").openStream());
				} else {
					System.out.println("No entry selected for view.");
					return;
				}
				
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
	private void editEntry(ActionEvent event) throws SQLException {
		try {
			Stage editStage = new Stage();
			FXMLLoader editLoader = new FXMLLoader();
			Pane editRoot = null;
			if (adminTab.equals("Patients")) {
				selectedPatient = patientTable.getSelectionModel().getSelectedItem();
				editRoot = (Pane)editLoader.load(getClass().getResource("/admin/edit/editFXML.fxml").openStream());
			} else if (adminTab.equals("Doctors")){
				selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
				editRoot = (Pane)editLoader.load(getClass().getResource("/admin/edit/editDoctorFXML.fxml").openStream());
			} else {
				System.out.println("No entry selected for edit.");
				return;
			}
			
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
	private void deleteEntry(ActionEvent event) throws SQLException {
		if (adminTab.equals("Patients")) {
			selectedPatient = patientTable.getSelectionModel().getSelectedItem();
			patientsToDel.add(selectedPatient);
		    patientTable.getItems().remove(selectedPatient);
		    selectedPatient = null;
		} else if (adminTab.equals("Doctors")) {
			selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
			doctorsToDel.add(selectedDoctor);
		    doctorTable.getItems().remove(selectedDoctor);
		    selectedDoctor = null;
		} else {
			System.out.println("No entry selected for deletion.");
			return;
		}
	}
	
	public static final LocalDate LOCAL_DATE (String dateString){
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	    LocalDate localDate = LocalDate.parse(dateString, formatter);
	    return localDate;
	}
}




//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//LocalDate bday = LocalDate.parse(this.birthday.getPromptText()); 
// LocalDate aday = LocalDate.parse("2018-11-27"); 


