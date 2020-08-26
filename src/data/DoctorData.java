package data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DoctorData {
	
	private IntegerProperty ID;
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty gender;
	private StringProperty email;
	private StringProperty birthday;
	private StringProperty department;
	private StringProperty patients;
	
	public DoctorData(int id, String firstName, String lastName, String gender, String email, String bday, String department, String patients) {
		this.ID = new SimpleIntegerProperty(id);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.gender = new SimpleStringProperty(gender);
		this.email = new SimpleStringProperty(email);
		this.birthday = new SimpleStringProperty(bday);
		this.department = new SimpleStringProperty(department);
		this.patients = new SimpleStringProperty(patients);
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
	
	public String getDepartment() {
		return department.get();
	}
	
	public String getPatients() {
		return patients.get();
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
	
	public void setDepartment(String department) {
		this.department.set(department);
	}
	
	public void setPatients(String patients) {
		this.patients.set(patients);
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
	
	public StringProperty departmentProperty() {
		return this.department;
	}
	
	public StringProperty patientsProperty() {
		return this.patients;
	}
}
