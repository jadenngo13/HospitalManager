package admin;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class PatientData {
	
	private StringProperty ID;
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty phone;
	private StringProperty birthday;
	
	public PatientData(String id, String firstName, String lastName, String phone, String bday) {
		this.ID = new SimpleStringProperty(id);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.phone = new SimpleStringProperty(phone);
		this.birthday = new SimpleStringProperty(bday);
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

	public String getPhone() {
		return phone.get();
	}

	public String getBirthday() {
		return birthday.get();
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

	public void setPhone(String phone) {
		this.phone.set(phone);
	}

	public void setBirthday(String birthday) {
		this.birthday.set(birthday);
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
	
	public StringProperty phoneProperty() {
		return this.phone;
	}
	
	public StringProperty birthdayProperty() {
		return this.birthday;
	}
}
