package api;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.HeaderParam;

import core.DBConnect;
import entity.User;

class UserUtill {
	public static User getUserByToken(String token) throws SQLException {
		DBConnect db = new DBConnect();
		String sql = "select * from user where token='" + token + "'";
		int count = db.countExecuteSQL(sql);
		if (count == 0) {
			return null;
		} else {
			ResultSet rs = db.executeSQL(sql);
			rs.next();
			return new User(rs.getString(1),rs.getString(2),rs.getString(5));
		}
	}

}