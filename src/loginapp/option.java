package loginapp;

public enum option {
	Admin, Patient;
	
	private option() {}
	
	public String value() {
		return name();
	}
	
	public static option fromValue(String val) {
		return valueOf(val);
	}
}
