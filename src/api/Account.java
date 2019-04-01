package api;

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

import com.google.gson.JsonObject;

import core.DBConnect;
import core.DEFINE;
import core.MD5;
import core.ResultAPI;
import entity.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@Path("/account")
public class Account {

	public Account() {
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Object login(@Context HttpServletResponse rep, String[] input) throws SQLException {
		//rep.setHeader("Access-Control-Allow-Origin", "*");
		//rep.setHeader("access-control-expose-headers","access-control-allow-origin,access-control-expose-headers,access-control-allow-headers,content-type,content-length,date,connection,x-final-url");
		//rep.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token");
		String userName = input[0];
		String pass = input[1];
		if (userName == null || pass == null) {
			return new ResultAPI(DEFINE.ERR_NOT_ENOUGH_INFO, "Khong co thong tin dang nhap");
		}
		String md5Pass = MD5.getMd5(pass);
		DBConnect db = new DBConnect();
		if (db.isConnect()) {
			String sql = "select role from user where username= '" + userName + "' and password= '" + md5Pass + "'";
			System.out.println(sql);
			ResultSet rs = db.executeSQL(sql);

			while (rs.next()) {
				Random rd = new Random();
				String stRandom = "";
				String sql1 = "";
				boolean hasToken;
				do {
					for (int i = 0; i < 20; i++) {
						stRandom += String.valueOf(rd.nextInt(9));
					}
					sql1 = "select token from user where token= '" + stRandom + "'";
					System.out.println(sql1);
					DBConnect db1 = new DBConnect();
					hasToken = db1.executeSQL(sql1).next();
				} while (hasToken);

				ArrayList<String> travene = new ArrayList<>();
				travene.add(rs.getString(1));
				travene.add(stRandom);
				String sql2 = "update user set token='" + stRandom + "'where username='" + userName + "'";
				System.out.println(sql2);
				int rs2 = db.executeUpdate(sql2);
				JsonObject retu = new JsonObject();
				retu.addProperty("errCode", DEFINE.SUCCESS);
				retu.addProperty("token", stRandom);
				retu.addProperty("role",Integer.parseInt(UserUtill.getUserByToken(stRandom).getRole()));
				
				return retu.toString();
			}
			return new ResultAPI(DEFINE.ERR_PASS_DONT_CORRECT, "Sai username hoac mat khau");
		}
		return new ResultAPI(DEFINE.ERR_DB_CONNECT, "ket noi du lieu");

	}

	@POST
	@Path("/register_patient")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultAPI registerPatient(@Context HttpServletResponse rep, @HeaderParam("token") String token,
			Object[] input) throws SQLException {
		rep.setHeader("access-control-allow-origin", "*");
		String sdt = String.valueOf(input[0]);
		String name = String.valueOf(input[1]);
		String birth = String.valueOf(input[2]);
		String gender = String.valueOf(input[3]);
		String insurrance = String.valueOf(input[4]);
		String add = String.valueOf(input[5]);
		if (Integer.parseInt(UserUtill.getUserByToken(token).getRole()) != DEFINE.ROLE_RECIP) {
			return new ResultAPI(DEFINE.ERR_USER_DONTCORRECT, "khong phai le tan");
		} else {
			DBConnect db = new DBConnect();
			String sql = "select username from user where username='" + sdt + "'";
			int count = db.countExecuteSQL(sql);
			String sqll = "select idBH from benhnhan where idBH='" + insurrance + "'";
			int count1 = db.countExecuteSQL(sqll);
			if (count == 1 || count1 == 1) {
				return new ResultAPI(DEFINE.ERR_USER_EXIST, "da ton tai");
			} else {
				String sql1 = "insert into user(username,password,role) value('" + sdt + "','" + MD5.getMd5("0000")
						+ "','" + DEFINE.ROLE_PATIENT + "')";
				System.out.println(sql1);
				int is = db.executeUpdate(sql1);
				if (is == 0) {
					return new ResultAPI(DEFINE.ERR_DB_CONNECT, "loi ket noi");
				}
				String sql2 = "select iduser from user where username='" + sdt + "'";
				ResultSet rs2 = db.executeSQL(sql2);
				rs2.next();
				String idUser = rs2.getString(1);
				String insert = "insert into benhnhan(name,ngaysinh,gioitinh,diachi,sodienthoai,idBH,idUser) value ('"
						+ name + "','" + birth + "','" + gender + "','" + add + "','" + sdt + "','" + insurrance + "','"
						+ idUser + "')";
				System.out.println(insert);
				int ins = db.executeUpdate(insert);
				return new ResultAPI(ins, "ok");
			}
		}
	}

	@POST
	@Path("/register_doctor")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultAPI registerDoctor(@Context HttpServletResponse rep, @HeaderParam("token") String token,
			Object[] input) throws SQLException {
		rep.setHeader("access-control-allow-origin", "*");
		String sdt = String.valueOf(input[0]);
		String name = String.valueOf(input[1]);
		String gender = String.valueOf(input[2]);
		String chuyenkhoa = String.valueOf(input[3]);
		if (Integer.parseInt(UserUtill.getUserByToken(token).getRole()) != DEFINE.ROLE_ADMIN) {
			return new ResultAPI(DEFINE.ERR_USER_DONTCORRECT, "khong cho phep ");
		} else {
			DBConnect db = new DBConnect();
			String sql = "select username from user where username='" + sdt + "'";
			int count = db.countExecuteSQL(sql);
			String sqll = "select idChuyenKhoa from chuyenkhoa where idChuyenKhoa='" + chuyenkhoa + "'";
			int count1 = db.countExecuteSQL(sqll);
			if (count == 1) {
				return new ResultAPI(DEFINE.ERR_USER_EXIST, "da ton tai");
			} else if (count1 == 0) {
				return new ResultAPI(DEFINE.ERR_CHUYENKHOA_DONT_EXIST, "chuyen khoa chua ton tai");
			} else {
				String sql1 = "insert into user(username,password,role) value('" + sdt + "','" + MD5.getMd5("0000")
						+ "','" + DEFINE.ROLE_DOCTOR + "')";
				System.out.println(sql1);
				int is = db.executeUpdate(sql1);
				if (is == 0) {
					return new ResultAPI(DEFINE.ERR_DB_CONNECT, "loi ket noi");
				}
				String sql2 = "select iduser from user where username='" + sdt + "'";
				ResultSet rs2 = db.executeSQL(sql2);
				rs2.next();
				String idUser = rs2.getString(1);
				String insert = "insert into bacsi(hoten,gender,sodienthoai,idPhongban,idUser) value ('" + name + "','"
						+ gender + "','" + sdt + "','" + chuyenkhoa + "','" + idUser + "')";
				System.out.println(insert);
				int ins = db.executeUpdate(insert);
				return new ResultAPI(DEFINE.SUCCESS, "ok");
			}
		}
	}

	@POST
	@Path("/update_pass")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultAPI updatePasswordUser(@Context HttpServletResponse rep, @HeaderParam("token") String token,
			String newpass) {
		rep.setHeader("access-control-allow-origin", "*");
		DBConnect db = new DBConnect();
		String sltoken = "select token from user where token='" + token + "'";
		System.out.println(sltoken);
		int count = db.countExecuteSQL(sltoken);
		if (count == 0) {
			return new ResultAPI(DEFINE.ERR_USER_DONTCORRECT, "tai khoan k dung");
		} else {
			String udpass = "update user set password='" + MD5.getMd5(newpass) + "' where token='" + token + "'";
			System.out.println(udpass);
			int rs = db.executeUpdate(udpass);
			return new ResultAPI(DEFINE.SUCCESS, "ok");
		}
	}

	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfomationUser(@Context HttpServletResponse rep, @HeaderParam("token") String token)
			throws SQLException {
		rep.setHeader("access-control-allow-origin", "*");
		DBConnect db = new DBConnect();
		User user = UserUtill.getUserByToken(token);
		if (user == null) {
			JsonObject hh = new JsonObject();
			hh.addProperty("errCode", DEFINE.ERR_LOGIN_FALSE);
			return hh.toString();
		} else if (Integer.parseInt(UserUtill.getUserByToken(token).getRole()) == DEFINE.ROLE_PATIENT) {
			String slPatient = "select * from benhnhan where idUser='" + user.getIdUser() + "'";
			ResultSet rs = db.executeSQL(slPatient);
			rs.next();
			JsonObject patient = new JsonObject();
			patient.addProperty("errCode", DEFINE.SUCCESS);
			patient.addProperty("name", rs.getString(2));
			patient.addProperty("birth", rs.getString(3));
			patient.addProperty("gender", rs.getString(4));
			patient.addProperty("add", rs.getString(5));
			patient.addProperty("phone", rs.getString(6));
			patient.addProperty("insurance", rs.getString(7));
			return patient.toString();
		} else {
			String slDoctor = "select * from bacsi where idUser='" + user.getIdUser() + "'";
			ResultSet rs = db.executeSQL(slDoctor);
			rs.next();
			JsonObject doctor = new JsonObject();
			doctor.addProperty("errCode", DEFINE.SUCCESS);
			doctor.addProperty("name", rs.getString(2));
			doctor.addProperty("gender", rs.getString(3));
			doctor.addProperty("phone", rs.getString(4));
			doctor.addProperty("chuyen khoa", rs.getString(5));
			return doctor.toString();
		}
	}

	@GET
	@Path("/getinfo")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfoByRecip(@Context HttpServletResponse rep, @HeaderParam("token") String token,
			@QueryParam("userName") String userName) throws NumberFormatException, SQLException {
		rep.setHeader("access-control-allow-origin", "*");
		DBConnect db = new DBConnect();
		if (Integer.parseInt(UserUtill.getUserByToken(token).getRole()) == DEFINE.ROLE_PATIENT) {
			JsonObject js = new JsonObject();
			js.addProperty("errCode", DEFINE.ERR_USER_DONTCORRECT);
			return js.toString();
		} else {
			DBConnect db1 = new DBConnect();
			String slrole = "select role from user where username='" + userName + "'";
			String slidUser = "select iduser from user where username='" + userName + "'";
			ResultSet result = db1.executeSQL(slidUser);
			result.next();
			String idUser = result.getString(1);
			int count = db1.countExecuteSQL(slrole);
			ResultSet rs = db1.executeSQL(slrole);
			rs.next();
			int role = Integer.parseInt(rs.getString(1));
			if (count == 0) {
				JsonObject js = new JsonObject();
				js.addProperty("errCode", DEFINE.ERR_USER_NOT_EXIST);
				return js.toString();
			} else {
				if (role == DEFINE.ROLE_PATIENT) {
					String slPatient = "select * from benhnhan where idUser='" + idUser + "'";
					ResultSet rs1 = db.executeSQL(slPatient);
					rs1.next();
					JsonObject patient = new JsonObject();
					patient.addProperty("errCode", DEFINE.SUCCESS);
					patient.addProperty("name", rs1.getString(2));
					patient.addProperty("birth", rs1.getString(3));
					patient.addProperty("gender", rs1.getString(4));
					patient.addProperty("add", rs1.getString(5));
					patient.addProperty("phone", rs1.getString(6));
					patient.addProperty("insurance", rs1.getString(7));
					return patient.toString();
				} else {
					String slDoctor = "select * from bacsi where idUser='" + idUser + "'";
					System.out.println(slDoctor);
					ResultSet rs1 = db1.executeSQL(slDoctor);
					rs1.next();
					JsonObject doctor = new JsonObject();
					doctor.addProperty("errCode", DEFINE.SUCCESS);
					doctor.addProperty("name", rs1.getString(2));
					doctor.addProperty("gender", rs1.getString(3));
					doctor.addProperty("phone", rs1.getString(4));
					doctor.addProperty("chuyen khoa", rs1.getString(5));
					return doctor.toString();
				}
			}
		}
	}

}
