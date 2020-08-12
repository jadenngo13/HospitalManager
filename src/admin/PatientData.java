package admin;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class PatientData {
	
	private StringProperty ID;
	private StringProperty FIRSTNAME;
	private StringProperty LASTNAME;
	private StringProperty PHONE;
	private StringProperty BIRTHDAY;
	
	public PatientData(String id, String firstName, String lastName, String phone, String bday) {
		this.ID = new SimpleStringProperty(id);
		this.FIRSTNAME = new SimpleStringProperty(firstName);
		this.LASTNAME = new SimpleStringProperty(lastName);
		this.PHONE = new SimpleStringProperty(phone);
		this.BIRTHDAY = new SimpleStringProperty(bday);
	}
	
	public StringProperty getID() {
		return ID;
	}

	public StringProperty getFirstName() {
		return FIRSTNAME;
	}

	public StringProperty getLastName() {
		return LASTNAME;
	}

	public StringProperty getPhone() {
		return PHONE;
	}

	public StringProperty getBirthday() {
		return BIRTHDAY;
	}

	public void setID(StringProperty id) {
		ID = id;
	}

	public void setFirstName(StringProperty firstName) {
		FIRSTNAME = firstName;
	}

	public void setLastName(StringProperty lastName) {
		LASTNAME = lastName;
	}

	public void setPhone(StringProperty phone) {
		PHONE = phone;
	}

	public void setBirthday(StringProperty birthday) {
		BIRTHDAY = birthday;
	}
	
	public StringProperty idProperty() {
		return this.ID;
	}
	
	public StringProperty firstNameProperty() {
		return this.ID;
	}
}
