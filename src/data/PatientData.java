package data;

import javafx.scene.control.CheckBox;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PatientData {
	
	private IntegerProperty ID;
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty gender;
	private StringProperty email;
	private StringProperty birthday;
	private StringProperty appDate;
	private StringProperty info;
	private IntegerProperty doctor;
	private CheckBox select;
	
	public PatientData(int id, String firstName, String lastName, String gender, String email, String bday, String appDate, String info, int doctor) {
		this.ID = new SimpleIntegerProperty(id);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.gender = new SimpleStringProperty(gender);
		this.email = new SimpleStringProperty(email);
		this.birthday = new SimpleStringProperty(bday);
		this.appDate = new SimpleStringProperty(appDate);
		this.info = new SimpleStringProperty(info);
		this.doctor = new SimpleIntegerProperty(doctor);
		this.select = new CheckBox();
	}
	
	public int getID() {
		return ID.get();
	}

	public String getFirstName() {
		return firstName.get();
	}

	public String getLastName() {
		return lastName.get();
	}

	public String getGender() {
		return gender.get();
	}
	
	public String getEmail() {
		return email.get();
	}

	public String getBirthday() {
		return birthday.get();
	}
	
	public String getAppDate() {
		return appDate.get();
	}
	
	public String getInfo() {
		return info.get();
	}
	
	public int getDoctor() {
		return doctor.get();
	}
	
	public CheckBox getSelect() {
		return this.select;
	}

	public void setID(int id) {
		this.ID.set(id);
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	
	public void setGender(String gender) {
		this.gender.set(gender);
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public void setBirthday(String birthday) {
		this.birthday.set(birthday);
	}
	
	public void setAppDate(String appDate) {
		this.appDate.set(appDate);
	}
	
	public void setInfo(String info) {
		this.info.set(info);
	}
	
	public void setDoctor(int doctor) {
		this.doctor.set(doctor);
	}
	
	public void setSelect(CheckBox select) {
		this.select = select;
	}
	
	public IntegerProperty idProperty() {
		return this.ID;
	}
	
	public StringProperty firstNameProperty() {
		return this.firstName;
	}
	
	public StringProperty lastNameProperty() {
		return this.lastName;
	}
	
	public StringProperty genderProperty() {
		return this.gender;
	}
	
	public StringProperty emailProperty() {
		return this.email;
	}
	
	public StringProperty birthdayProperty() {
		return this.birthday;
	}
	
	public StringProperty appDateProperty() {
		return this.appDate;
	}
	
	public StringProperty infoProperty() {
		return this.info;
	}
	
	public IntegerProperty doctorProperty() {
		return this.doctor;
	}
}
