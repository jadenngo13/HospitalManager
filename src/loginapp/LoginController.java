package loginapp;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import patient.PatientController;

public class LoginController implements Initializable {
	
	LoginModel loginModel = new LoginModel();

	@FXML 
	private Label dbStatus;
	@FXML 
	private TextField username;
	@FXML 
	private PasswordField password;
	@FXML 
	private ComboBox<option> combo;
	@FXML 
	private Button loginButton;
	@FXML 
	private Label loginStatus;
	
	public static Connection conn;
	
	
	public void initialize(URL url, ResourceBundle rb) {
		if (this.loginModel.isDBConnected()) {
			this.dbStatus.setText("Connected");
		} else {
			this.dbStatus.setText("Not Connected");
		}
		this.combo.setItems(FXCollections.observableArrayList(option.values() ));
	}
	
	@FXML
	public void Login(ActionEvent event) {
		try {
			if (this.loginModel.isLogin(this.username.getText(), this.password.getText(), ((option)this.combo.getValue()).toString())) {
				Stage stage = (Stage)this.loginButton.getScene().getWindow();
				stage.close();
				switch(((option)this.combo.getValue()).toString()) {
					case "Patient":
						patientLogin();
						break;
					case "Admin":
						adminLogin();
						break;
					case "Doctor":
						doctorLogin();
						break;
				}
			} else {
				this.loginStatus.setText("Incorrect");
			}
		} catch (Exception localException) {
			
		}
	}
	
	public void patientLogin() {
		try {
			Stage patientStage = new Stage();
			FXMLLoader patientLoader = new FXMLLoader();
			Pane patientRoot = (Pane)patientLoader.load(getClass().getResource("/patient/patientFXML.fxml").openStream());
			
			Scene userScene = new Scene(patientRoot);
			patientStage.setScene(userScene);
			patientStage.setTitle("Patient Menu");
			patientStage.setResizable(false);
			patientStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void adminLogin() {
		try {
			Stage adminStage = new Stage();
			FXMLLoader adminLoader = new FXMLLoader();
			Pane adminRoot = (Pane)adminLoader.load(getClass().getResource("/admin/adminFXML.fxml").openStream());
			
			Scene adminScene = new Scene(adminRoot);
			adminStage.setScene(adminScene);
			adminStage.setTitle("Admin Menu");
			adminStage.setResizable(false);
			adminStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void doctorLogin() {
		try {
			Stage doctorStage = new Stage();
			FXMLLoader doctorLoader = new FXMLLoader();
			Pane doctorRoot = (Pane)doctorLoader.load(getClass().getResource("/doctor/doctorFXML.fxml").openStream());
			
			Scene doctorScene = new Scene(doctorRoot);
			doctorStage.setScene(doctorScene);
			doctorStage.setTitle("Doctor Menu");
			doctorStage.setResizable(false);
			doctorStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}





