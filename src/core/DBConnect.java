package core;
import java.sql.*;  

public class DBConnect {
static String HOST = "13.70.25.1";
static String PORT = "3306";
static String DBNAME = "doanhttt";
static String USERNAME = "doanhttt";
static String PASS = "emkay";
Connection con = null;
Statement state = null;

public DBConnect() {
	try {
		Class.forName("com.mysql.jdbc.Driver");
		this.con = DriverManager.getConnection("jdbc:mysql://"+HOST+":"+PORT+"/"+DBNAME, USERNAME, PASS);
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

public boolean isConnect() {
	try {
		return !this.con.isClosed();
	} catch (SQLException e) {
		return false;
	}
}

public ResultSet executeSQL(String sql) {
	try {
		return this.state.executeQuery(sql);
	} catch (SQLException e) {
		System.out.println(e.getMessage());
		return null;
	}
}
public int executeUpdate(String sql) {
	try {
		return this.state.executeUpdate(sql);
	} catch (SQLException e) {
		System.out.println(e.getMessage());
		return 0;
	}
}
public int countExecuteSQL(String sql) {
	try {
		ResultSet rs = this.state.executeQuery(sql);
		int count = 0;
		while(rs.next()) {
			count++;
		}
		return count;
	} catch (SQLException e) {
		System.out.println(e.getMessage());
		return 0;
	}
}



}