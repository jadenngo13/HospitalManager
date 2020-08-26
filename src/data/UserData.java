package data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserData {
	
	private StringProperty user;
	private StringProperty pass;
	private StringProperty department;
	private IntegerProperty ID;
	
	public UserData(String user, String pass, String department, int id) {
		this.user = new SimpleStringProperty(user);
		this.pass = new SimpleStringProperty(pass);
		this.department = new SimpleStringProperty(department);
		this.ID = new SimpleIntegerProperty(id);
	}

	public String getUser() {
		return user.get();
	}

	public String getPass() {
		return pass.get();
	}
	
	public String getDepartment() {
		return department.get();
	}
	
	public int getID() {
		return ID.get();
	}

	public void setUser(String user) {
		this.user.set(user);
	}

	public void setPass(String pass) {
		this.pass.set(pass);
	}
	
	public void setDepartment(String department) {
		this.department.set(department);
	}
	
	public StringProperty userProperty() {
		return this.user;
	}
	
	public StringProperty passProperty() {
		return this.pass;
	}
	
	public StringProperty departmentProperty() {
		return this.department;
	}
	
	public IntegerProperty idProperty() {
		return this.ID;
	}
}
