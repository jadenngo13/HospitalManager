package sql;

public class SqlQueries {
	public static String sqlLoadPatients = "SELECT * FROM patients";
	public static String sqlLoadDoctors = "SELECT * FROM doctors";
	public static String sqlLoadUsers = "SELECT * FROM login";
	
	public static String sqlInsertPatient = "INSERT INTO patients(first_name, last_name, gender, email, birthday, appointment_date, info, doctor) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	public static String sqlInsertDoctor = "INSERT INTO doctors(first_name, last_name, gender, email, birthday, department, patients) VALUES(?, ?, ?, ?, ?, ?, ?)";

	public static String sqlDelPatients = "DELETE FROM patients WHERE id=?";
	public static String sqlDelDoctors = "DELETE FROM doctors WHERE id=?";
	public static String sqlDelUsers = "DELETE FROM login WHERE department=? AND id=?";
	
	public static String sqlUpdatePatients = "UPDATE patients SET doctor=? WHERE id=?";
	public static String sqlUpdateUsers = "UPDATE login SET username=?, password=?, department=? WHERE department=? AND id=?";
	public static String sqlUpdateDoctorsPatient = "UPDATE patients SET doctor=? WHERE id=?";
	public static String sqlUpdateDoctorsPatient1 = "UPDATE patients SET doctor=? WHERE id=?";
	public static String sqlUpdatePatientsDoctor = "UPDATE doctors SET patients=? WHERE id=?";
	
	public static String sqlGetDoctorPatients = "SELECT patients FROM doctors WHERE id=?";
	public static String sqlGetPatientsDoctor = "SELECT * FROM doctors WHERE id=?";
	public static String sqlGetPatientFromID = "SELECT * FROM patients WHERE id=?";
	public static String sqlGetDoctorFromID = "SELECT * FROM doctors WHERE id=?";
	public static String sqlGetRecentPatient = "SELECT * FROM patients WHERE id = (SELECT MAX(id) FROM patients);";
	public static String sqlGetRecentDoctor = "SELECT * FROM doctors WHERE id = (SELECT MAX(id) FROM doctors);";

	public static String sqlSave = "UPDATE doctors SET first_name=?, last_name=?, gender=?, email=?, birthday=?, department=?, patients=? WHERE id=?";
	public static String sqlCreateLogin = "INSERT INTO login(username, password, department, id) VALUES(?, ?, ?, ?)";
}
