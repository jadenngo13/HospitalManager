package patient;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import admin.AdminController;
import data.DoctorData;
import data.PatientData;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import loginapp.LoginModel;

public class PatientController implements Initializable {

	@FXML
	private TextField id;
	@FXML
	private TextField name;
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
	private TableView<DoctorData> doctorTable;
	@FXML
	private TableColumn<DoctorData, Integer> idColumn;
	@FXML
	private TableColumn<DoctorData, String> fnColumn;
	@FXML
	private TableColumn<DoctorData, String> lnColumn;
	@FXML
	private TableColumn<DoctorData, String> genderColumn;
	@FXML
	private TableColumn<DoctorData, String> emailColumn;
	@FXML
	private TableColumn<DoctorData, String> birthdayColumn;
	@FXML
	private TableColumn<DoctorData, String> departmentColumn;
	@FXML
	private Button logoutButton;
	
	private ObservableList<DoctorData> doctorData;
	private PatientData user;
	
	private ResultSet rs;
	private PreparedStatement stmt;
	private Connection conn;
	
	public void initialize(URL url, ResourceBundle rb) {
		rs = null;
		stmt = null;
		try {
			conn = dbConnection.conn;
			
			
			stmt = conn.prepareStatement(AdminController.sqlGetPatientFromID);
			stmt.setInt(1, LoginModel.patID);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.user = new PatientData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getInt(9));
			}
			
			stmt = conn.prepareStatement(AdminController.sqlGetPatientsDoctor);
			stmt.setInt(1, user.getDoctor());
			
			this.doctorData = FXCollections.observableArrayList();
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.doctorData.add(new DoctorData(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
			}
			
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}

		this.id.setText(Integer.toString(user.getID()));
		this.name.setText(user.getFirstName() + user.getLastName());
		this.gender.setText(user.getGender());
		this.email.setText(user.getEmail());
		this.birthday.setValue(AdminController.LOCAL_DATE(user.getBirthday()));
		this.appDate.setValue(AdminController.LOCAL_DATE(user.getBirthday()));
		this.info.setText(user.getInfo());
		
		this.idColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, Integer>("ID"));
		this.fnColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("firstName"));
		this.lnColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("lastName"));
		this.genderColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("gender"));
		this.emailColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("email"));
		this.birthdayColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("birthday"));
		this.departmentColumn.setCellValueFactory(new PropertyValueFactory<DoctorData, String>("department"));
		this.doctorTable.setItems(null);
		this.doctorTable.setItems(doctorData);
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
