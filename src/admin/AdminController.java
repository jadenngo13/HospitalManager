package admin;

import java.io.IOException;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import loginapp.LoginController;
import loginapp.LoginModel;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;
import java.util.Random;

import data.PatientData;
import data.UserData;
import data.DoctorData;

public class AdminController implements Initializable {
	
	/***** Patients  Tab *****/
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
	private TableColumn<PatientData, Integer> docColumn;
	
	/***** Doctors  Tab *****/
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
	private TableColumn<PatientData, Integer> idColumn3;
	@FXML
	private TableColumn<PatientData, String> fnColumn3;
	@FXML
	private TableColumn<PatientData, String> lnColumn3;
	@FXML
	private TableColumn<PatientData, String> genderColumn3;
	@FXML
	private TableView<DoctorData> doctorTable; // Main doctor table
	@FXML
	private TableColumn<DoctorData, Integer> idColumn2;
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

	/***** Admin  Tab *****/
	@FXML
	private TableView<UserData> adminTable;
	@FXML
	private TableColumn<UserData, String> userColumn;
	@FXML
	private TableColumn<UserData, String> passColumn;
	@FXML
	private TableColumn<UserData, String> departmentColumn2;
	@FXML
	private TableColumn<UserData, Integer> idColumn4;
	
	@FXML
	private Button logoutButton;

	/***** Variables *****/
	private ObservableList<PatientData> patientData;
	private ObservableList<DoctorData> doctorData;
	private ObservableList<UserData> userData;
	
	private ArrayList<PatientData> patientsToDel = new ArrayList<PatientData>();
	private ArrayList<DoctorData> doctorsToDel = new ArrayList<DoctorData>();
	
	public static PatientData selectedPatient;
	public static DoctorData selectedDoctor;
	public static UserData selectedUser;
	
	private String adminTab = "Patients";
	
	private ResultSet rs;
	private PreparedStatement stmt;
	private Connection conn;
	
	public static String sqlLoadPatients = "SELECT * FROM patients";
	public static String sqlLoadDoctors = "SELECT * FROM doctors";
	public static String sqlLoadUsers = "SELECT * FROM login";
	public static String sqlInsertPatient = "INSERT INTO patients(first_name, last_name, gender, email, birthday, appointment_date, info, doctor) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	public static String sqlInsertDoctor = "INSERT INTO doctors(first_name, last_name, gender, email, birthday, department, patients) VALUES(?, ?, ?, ?, ?, ?, ?)";
	public static String sqlDelPatients = "DELETE FROM patients WHERE id=?";
	public static String sqlDelDoctors = "DELETE FROM doctors WHERE id=?";
	public static String sqlDelUsers = "DELETE FROM login WHERE department=? AND id=?";
	public static String sqlUpdatePatients = "UPDATE patients SET doctor=? WHERE id=?";
	public static String sqlUpdateUsers = "UPDATE login SET username=?, password=?, department=? WHERE department=? AND id=?";
	public static String sqlUpdateDoctorsPatient = "UPDATE patients SET doctor=? WHERE id=?";
	public static String sqlUpdateDoctorsPatient1 = "UPDATE patients SET doctor=? WHERE id=?";
	public static String sqlUpdatePatientsDoctor = "UPDATE doctors SET patients=? WHERE id=?";
	public static String sqlGetDoctorPatients = "SELECT patients FROM doctors WHERE id=?";
	public static String sqlGetPatientsDoctor = "SELECT * FROM doctors WHERE id=?";
	public static String sqlGetPatientFromID = "SELECT * FROM patients WHERE id=?";
	public static String sqlGetDoctorFromID = "SELECT * FROM doctors WHERE id=?";
	public static String sqlGetRecentPatient = "SELECT * FROM patients WHERE id = (SELECT MAX(id) FROM patients);";
	public static String sqlGetRecentDoctor = "SELECT * FROM doctors WHERE id = (SELECT MAX(id) FROM doctors);";
	public static String sqlSave = "UPDATE doctors SET first_name=?, last_name=?, gender=?, email=?, birthday=?, department=?, patients=? WHERE id=?";
	public static String sqlCreateLogin = "INSERT INTO login(username, password, department, id) VALUES(?, ?, ?, ?)";
	
	public void initialize(URL url, ResourceBundle rb) {
		rs = null;
		stmt = null;
		
		try {
			conn = dbConnection.conn;
			
			// Load patients
			this.patientData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(sqlLoadPatients);
			while (rs.next()) {
				this.patientData.add(new PatientData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getInt(9)));
			}
				
			// Load doctors
			this.doctorData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(sqlLoadDoctors);
			while (rs.next()) {
				this.doctorData.add(new DoctorData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
			}
			
			this.userData = FXCollections.observableArrayList();
			rs = conn.createStatement().executeQuery(sqlLoadUsers);
			while (rs.next()) {
				this.userData.add(new UserData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	private void adminTab(Event event) throws SQLException {
		adminTab = "Admin";
		selectedPatient = null;
		selectedDoctor = null;
		patientsToDel.clear();
		doctorsToDel.clear();
	}
	@FXML
	private void refreshData(ActionEvent event) throws SQLException {
		try {
			
			if (adminTab.equals("Patients")) {
				this.patientData = FXCollections.observableArrayList();
				rs = conn.createStatement().executeQuery(sqlLoadPatients);
				while (rs.next()) {
					this.patientData.add(new PatientData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getInt(9)));
				}
			} else if (adminTab.equals("Doctors")) {
				this.doctorData = FXCollections.observableArrayList();
				rs = conn.createStatement().executeQuery(sqlLoadDoctors);
				while (rs.next()) {
					this.doctorData.add(new DoctorData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
				}
			} else if (adminTab.equals("Admin")) {
				this.userData = FXCollections.observableArrayList();
				rs = conn.createStatement().executeQuery(sqlLoadUsers);
				while (rs.next()) {
					this.userData.add(new UserData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4)));
				}
			} else {
				System.out.println("No tab is selected for refresh data.");
				return;
			}
			patientsToDel.clear();
			doctorsToDel.clear();
			
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		if (adminTab.equals("Patients")) {
			this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, Integer>("ID"));
			this.fnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
			this.lnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("lastName"));
			this.genderColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("gender"));
			this.emailColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("email"));
			this.birthdayColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("birthday"));
			this.appDateColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("appDate"));
			this.infoColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("info"));
			this.docColumn.setCellValueFactory(new PropertyValueFactory<PatientData, Integer>("doctor"));
			this.patientTable.setItems(null);
			this.patientTable.setItems(patientData);
		} else if (adminTab.equals("Doctors")) {
			this.selectColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("select"));
			this.idColumn3.setCellValueFactory(new PropertyValueFactory<PatientData, Integer>("ID"));
			this.fnColumn3.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
			this.lnColumn3.setCellValueFactory(new PropertyValueFactory<PatientData, String>("lastName"));
			this.genderColumn3.setCellValueFactory(new PropertyValueFactory<PatientData, String>("gender"));
			this.patientsTable1.setItems(null);
			this.patientsTable1.setItems(patientData);
			
			this.idColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, Integer>("ID"));
			this.fnColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("firstName"));
			this.lnColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("lastName"));
			this.genderColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("gender"));
			this.emailColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("email"));
			this.birthdayColumn2.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("birthday"));
			this.departmentColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("department"));
			this.patientsColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("patients"));
			this.doctorTable.setItems(null);
			this.doctorTable.setItems(doctorData);
		} else if (adminTab.equals("Admin")) {
			this.userColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("user"));
			this.passColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("pass"));
			this.departmentColumn2.setCellValueFactory(new PropertyValueFactory<UserData, String>("department"));
			this.idColumn4.setCellValueFactory(new PropertyValueFactory<UserData, Integer>("ID"));
			this.adminTable.setItems(null);
			this.adminTable.setItems(userData);
		}
	}
	
	@FXML
	private void saveData(ActionEvent event) throws SQLException {
		if (adminTab.equals("Patients")) {
			try {
				for (PatientData patient : patientsToDel) {
					
					// Unassign patient from assigned doctor
					stmt = conn.prepareStatement(sqlUpdatePatientsDoctor);
				    for (DoctorData doctor : doctorData) {
						String[] patsArr = doctor.getPatients().split(",");
						StringBuilder newPats = new StringBuilder();
						for (String patID : patsArr) {
							if (Integer.valueOf(patID) != patient.getID()) {
								newPats.append(patID + ",");
							}
						}
						stmt.setString(1, newPats.toString()); //newPats
						stmt.setInt(2, doctor.getID());
						stmt.execute();
					}
				    
					stmt = conn.prepareStatement(sqlDelPatients);
				    stmt.setInt(1, patient.getID());
				    stmt.execute();
				    
				    stmt = conn.prepareStatement(sqlDelUsers);
				    stmt.setString(1, "Patient");
				    stmt.setInt(2, patient.getID());
				    stmt.execute();
				}
				patientsToDel.clear();
			} catch (SQLException e) {
				System.err.println("Error: " + e);
			}
		} else if (adminTab.equals("Doctors")){
			for (DoctorData doctor : doctorsToDel) {
				try {
					
					stmt = conn.prepareStatement(sqlDelDoctors);
				    stmt.setInt(1, doctor.getID());
				    stmt.execute();
				    
				    // Unassign doctor from all assigned patients
				    stmt = conn.prepareStatement(sqlUpdateDoctorsPatient);
				    stmt.setInt(1, -1);
				    stmt.setInt(2, doctor.getID());
				    stmt.execute();
				    
				    stmt = conn.prepareStatement(sqlDelUsers);
				    stmt.setString(1, "Doctor");
				    stmt.setInt(2, doctor.getID());
				    stmt.execute();
				} catch (SQLException e) {
					System.err.println("Error: " + e);
				}
			}
			doctorsToDel.clear();
		} else {
			return;
		}
	}

	@FXML
	private void addEntry(ActionEvent event) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			
			if (adminTab.equals("Patients")) { //SQL query inserts that differ for patients
				stmt = conn.prepareStatement(sqlInsertPatient);	
				stmt.setString(1, this.firstName.getText());
				stmt.setString(2, this.lastName.getText());
				stmt.setString(3, this.gender.getText());
				stmt.setString(4, this.email.getText());
				LocalDate bday = this.birthday.getValue();
				if (bday != null) {
				    stmt.setString(5, formatter.format(bday));
				}
				LocalDate aday = this.appDate.getValue(); 
				if (aday != null) {
					stmt.setString(6, formatter.format(aday));
				}
				stmt.setString(7, this.info.getText());
				stmt.setInt(8, -1);
				stmt.execute();
				
				rs = conn.createStatement().executeQuery(sqlGetRecentPatient);
				int newUserId = -1;
				if (rs.next()) {
					newUserId = rs.getInt(1);
				}
				createUser(newUserId, this.firstName.getText());
			} else if (adminTab.equals("Doctors")) { // SQL query inserts that differ for doctors
				stmt = conn.prepareStatement(sqlInsertDoctor);
				stmt.setString(1, this.firstName2.getText());
				stmt.setString(2, this.lastName2.getText());
				stmt.setString(3, this.gender2.getText());
				stmt.setString(4, this.email2.getText());
				LocalDate bday = this.birthday2.getValue();
				if (bday != null) {
				    stmt.setString(5, formatter.format(bday));
				}
				stmt.setString(6, this.department.getText());
				
				StringBuilder pats = new StringBuilder();
				for (PatientData patient : patientData) {
					if (patient.getSelect().isSelected()) {
						pats.append(patient.getID() + ",");
					}
				}
				stmt.setString(7, pats.toString());
				stmt.execute();
				
				rs = conn.createStatement().executeQuery(sqlGetRecentDoctor);
				int newDocID = -1;
				if (rs.next()) {
					newDocID = rs.getInt(1);
				}
				
				// Update patients
				String[] patsArr = pats.toString().split(",");
				for (String id : patsArr) {
					stmt = conn.prepareStatement(sqlUpdatePatients);
					stmt.setInt(1, newDocID);
					stmt.setString(2, id);
					stmt.execute();
				}
				
				createUser(newDocID, this.firstName2.getText());
			} else {
				System.out.println("No entry selected for add.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void clearEntry(ActionEvent event) throws SQLException {
		if (adminTab.equals("Patients")) {
			this.firstName.setText(null);
			this.lastName.setText(null);
			this.gender.setText(null);
			this.email.setText(null);
			this.birthday.setValue(null);
			this.appDate.setValue(null);
			this.info.setText(null);
		} else if (adminTab.equals("Doctors")) {
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
				if (selectedPatient != null)
					editRoot = (Pane)editLoader.load(getClass().getResource("/admin/edit/editFXML.fxml").openStream());
			} else if (adminTab.equals("Doctors")){
				selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
				if (selectedDoctor != null)
					editRoot = (Pane)editLoader.load(getClass().getResource("/admin/edit/editDoctorFXML.fxml").openStream());
			} else if (adminTab.equals("Admin")) {
				selectedUser = adminTable.getSelectionModel().getSelectedItem();
				if (selectedUser != null)
					editRoot = (Pane)editLoader.load(getClass().getResource("/admin/edit/editUserFXML.fxml").openStream());
			} else {
				System.out.println("No entry selected for edit.");
				return;
			}
			
			if (editRoot != null) {
				Scene editScene = new Scene(editRoot);
				editStage.setScene(editScene);
				editStage.setTitle("Edit Menu");
				editStage.setResizable(false);
				editStage.show();
			} else {
				System.out.println("Edit root null. Cannot edit since no entry given.");
			}
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
	
	private void createUser(int id, String name) {
		try {
			if (adminTab.equals("Patients")) {
				stmt = conn.prepareStatement(sqlCreateLogin);
				stmt.setString(1, name);
				stmt.setString(2, generateRandomString(7));
				stmt.setString(3, "Patient");
				stmt.setInt(4, id);
				stmt.execute();
			} else if (adminTab.equals("Doctors")) {
				stmt = conn.prepareStatement(sqlCreateLogin);
				stmt.setString(1, name);
				stmt.setString(2, generateRandomString(7));
				stmt.setString(3, "Doctor");
				stmt.setInt(4, id);
				stmt.execute();
			} else {
				System.out.println("No tab selected to allow creating of user");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static final LocalDate LOCAL_DATE (String dateString){
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	    LocalDate localDate = LocalDate.parse(dateString, formatter);
	    return localDate;
	}
	
	public String generateRandomString(int length) {
	    int leftLimit = 97; // Letter 'a'
	    int rightLimit = 122; // Letter 'z'
	    int len = length;
	    Random random = new Random();
	 
	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(len)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    
	    return generatedString;
	}
}


