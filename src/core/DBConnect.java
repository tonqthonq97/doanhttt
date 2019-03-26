package core;
import java.sql.*;  

public class DBConnect {
static String HOST = "13.70.25.1";
static String PORT = "3306";
static String DBNAME = "doanHTTT";
static String USERNAME = "doanhttt";
static String PASS = "emkay";
Connection con = null;
Statement state = null;

public DBConnect() {
	try {
		Class.forName("com.mysql.jdbc.Driver");
		this.con = DriverManager.getConnection("jdbc:mysql://"+HOST+":"+PORT, USERNAME, PASS);
		} catch (Exception e ) {
			System.out.println(e.toString());
		}
	if(con!=null) {
		System.out.println("Connected");
		try {
			this.state = this.con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
}

public ResultSet executeSQL(String sql) {
	try {
		return this.state.executeQuery(sql);
	} catch (SQLException e) {
		return null;
	}
}

public static void main(String[] args) {
	DBConnect db = new DBConnect();
}

}