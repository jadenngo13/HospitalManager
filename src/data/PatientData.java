package data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class PatientData {
	
	private StringProperty ID;
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty gender;
	private StringProperty email;
	private StringProperty birthday;
	private StringProperty appDate;
	private StringProperty info;
	
	public PatientData(String id, String firstName, String lastName, String gender, String email, String bday, String appDate, String info) {
		this.ID = new SimpleStringProperty(id);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.gender = new SimpleStringProperty(gender);
		this.email = new SimpleStringProperty(email);
		this.birthday = new SimpleStringProperty(bday);
		this.appDate = new SimpleStringProperty(appDate);
		this.info = new SimpleStringProperty(info);
	}
	
	public String getID() {
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

	public void setID(String id) {
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
	
	public StringProperty idProperty() {
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
}
