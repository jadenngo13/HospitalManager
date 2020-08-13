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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;

public class AdminController implements Initializable {
	@FXML 
	private TextField id;
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField phone;
	@FXML
	private DatePicker birthday;
	@FXML
	private TableView<PatientData> patientTable;
	@FXML
	private TableColumn<PatientData, String> idColumn;
	@FXML
	private TableColumn<PatientData, String> fnColumn;
	@FXML
	private TableColumn<PatientData, String> lnColumn;
	@FXML
	private TableColumn<PatientData, String> phoneColumn;
	@FXML
	private TableColumn<PatientData, String> birthdayColumn;
	
	private dbConnection dc;
	private ObservableList<PatientData> data;
	
	private String sqlLoad = "SELECT * FROM patients";
	String sqlInsert = "INSERT INTO patients(id, first_name, last_name, phone, birthday) VALUES (?, ?, ?, ?, ?)";
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
	}
	
	@FXML
	private void loadPatientData(ActionEvent event) throws SQLException {
		try {
			Connection conn = dbConnection.getConnection();
			this.data = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery(sqlLoad);
			
			while (rs.next()) {
				this.data.add(new PatientData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		
		this.idColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("ID"));
		this.fnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("firstName"));
		this.lnColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("lastName"));
		this.phoneColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("phone"));
		this.birthdayColumn.setCellValueFactory(new PropertyValueFactory<PatientData, String>("birthday"));
		
		this.patientTable.setItems(null);
		this.patientTable.setItems(data);
	}
	 
	@FXML
	private void addPatient(ActionEvent event) {
		try {
			Connection conn = dbConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlInsert);	
			
			stmt.setString(1, this.id.getText());
			stmt.setString(2, this.firstName.getText());
			stmt.setString(3, this.lastName.getText());
			stmt.setString(4, this.phone.getText());
			stmt.setString(5, this.birthday.getEditor().getText());
			
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
		this.phone.setText("");
		this.birthday.setValue(null);
	}
}





