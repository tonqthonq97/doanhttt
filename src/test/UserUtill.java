package test;

import java.sql.ResultSet;
import java.sql.SQLException;

import core.DBConnect;

class UserUtill{
	
	public static int getRoleByToken(String token ) throws SQLException {
		DBConnect db=new DBConnect();
		String sql="select role from user where token='"+token+"'";
		int count= db.countExecuteSQL(sql);
		if(count==0) {
			return 0;
		}else {
			ResultSet rs=db.executeSQL(sql);
			rs.next();
			return Integer.parseInt(rs.getString(1));
		}
	}
}