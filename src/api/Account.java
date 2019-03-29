package test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import core.DBConnect;
import core.DEFINE;
import core.MD5;
import core.ResultAPI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;



@Path("/account")
public class Account {
	
	public Account() {
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultAPI login(String [] input) throws SQLException {
		String userName=input[0];
		String pass = input[1];
		if(userName==null||pass==null) {
			return new ResultAPI(3, "Khong co thong tin dang nhap");
		}
		String md5Pass = MD5.getMd5(pass);
		DBConnect db=new DBConnect();
		if(db.isConnect()) {
			String sql="select role from user where username= '" + userName+"' and password= '"+md5Pass+"'";
			System.out.println(sql);
			ResultSet rs=db.executeSQL(sql);
			
			while(rs.next()) {			
				Random rd=new Random();
				String stRandom = "";
				String sql1="";
				boolean hasToken ;
				do {
				for(int i =0; i<20; i++) {
					stRandom+=String.valueOf(rd.nextInt(9));
				}
				sql1="select token from user where token= '"+stRandom+"'";
				System.out.println(sql1);
				DBConnect db1 = new DBConnect();
				hasToken=db1.executeSQL(sql1).next();
				} while(hasToken);
				
				ArrayList<String> travene = new ArrayList<>();
				travene.add(rs.getString(1));
				travene.add(stRandom);
				String sql2="update user set token='"+stRandom +"'where username='"+userName+"'";
				System.out.println(sql2);
				int rs2=db.executeUpdate(sql2);
				return new ResultAPI(1, travene);
			}
		
		}
		return new ResultAPI(1, "khong dung");
		
	}
	
	@POST
	@Path("/register_patient")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultAPI registerPatient (Object [] input) throws SQLException {
		String sdt=String.valueOf(input[0]);
		String name=String.valueOf(input[1]);
		String birth=String.valueOf(input[2]);
		String gender=String.valueOf(input[3]);
		String insurrance=String.valueOf(input[4]);
		String add=String.valueOf(input[5]);
		
		DBConnect db=new DBConnect();
		String sql="select username from user where username='"+sdt+"'";
		int count=db.countExecuteSQL(sql);
		String sqll="select idBH from benhnhan where idBH='"+insurrance+"'";
		int count1=db.countExecuteSQL(sqll);
		if(count==1||count1==1) {
			return new ResultAPI(DEFINE.ERR_USER_EXIST,"da ton tai");
		}
		else {
			String sql1="insert into user(username,password,role) value('"+sdt+"','"+MD5.getMd5("0000")+"','"+DEFINE.ROLE_PATIENT+"')";
			System.out.println(sql1);
			int is=db.executeUpdate(sql1);
			if(is==0) {
				return new ResultAPI(DEFINE.ERR_DB_CONNECT, "loi ket noi");
			}
			String sql2="select iduser from user where username='"+sdt+"'";
			ResultSet rs2=db.executeSQL(sql2);
			rs2.next();
			String idUser=rs2.getString(1);
			String insert="insert into benhnhan(name,ngaysinh,gioitinh,diachi,sodienthoai,idBH,idUser) value ('"
					+name+"','"+birth+"','"+gender+"','"+add+"','"+sdt+"','"+insurrance+"','"+idUser+"')";
			System.out.println(insert);
			int ins=db.executeUpdate(insert);
			return new ResultAPI(ins, "ok");
		}
				}

	
	@POST
	@Path("/register_doctor")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultAPI registerDoctor(Object[] input) throws SQLException {
		String sdt=String.valueOf(input[0]);
		String name=String.valueOf(input[1]);
		String gender=String.valueOf(input[2]);
		String chuyenkhoa=String.valueOf(input[3]);
		
		DBConnect db=new DBConnect();
		String sql="select username from user where username='"+sdt+"'";
		int count=db.countExecuteSQL(sql);
		String sqll="select idChuyenKhoa from chuyenkhoa where idChuyenKhoa='"+chuyenkhoa+"'";
		int count1=db.countExecuteSQL(sqll);
		if(count==1) {
			return new ResultAPI(DEFINE.ERR_USER_EXIST,"da ton tai");
		}
		else if(count1==0) {
			return new ResultAPI(DEFINE.ERR_CHUYENKHOA_DONT_EXIST, "chuyen khoa chua ton tai");
		}
		else {
			String sql1="insert into user(username,password,role) value('"+sdt+"','"+MD5.getMd5("0000")+"','"+DEFINE.ROLE_DOCTOR+"')";
			System.out.println(sql1);
			int is=db.executeUpdate(sql1);
			if(is==0) {
				return new ResultAPI(DEFINE.ERR_DB_CONNECT, "loi ket noi");
			}
			String sql2="select iduser from user where username='"+sdt+"'";
			ResultSet rs2=db.executeSQL(sql2);
			rs2.next();
			String idUser=rs2.getString(1);
			String insert="insert into bacsi(hoten,gender,sodienthoai,idPhongban,idUser) value ('"
					+name+"','"+gender+"','"+sdt+"','"+chuyenkhoa+"','"+idUser+"')";
			System.out.println(insert);
			int ins=db.executeUpdate(insert);
			return new ResultAPI(ins, "ok");
		}
	}
	
	
	@POST
	@Path("/update_pass")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultAPI updatePasswordUser(String[]input) {
		String token=input[0];
		String newpass=input[1];
		DBConnect db=new DBConnect();
		String sltoken="select token from user where token='"+token+"'";
		System.out.println(sltoken);
		int count=db.countExecuteSQL(sltoken);
		if(count==0) {
			return new ResultAPI(DEFINE.ERR_USER_DONTCORRECT, "tai khoan k dung");
		}
		else {
			String udpass="update user set pass='"+newpass+"' where token='"+token+"'";
			System.out.println(udpass);
			int rs=db.executeUpdate(udpass);
			return new ResultAPI(rs, "ok"); 
		}
	}
}



