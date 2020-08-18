package loginapp;

public enum option {
	Admin, Patient, Doctor;
	
	private option() {}
	
	public String value() {
		return name();
	}
	
	public static option fromValue(String val) {
		return valueOf(val);
	}
}
