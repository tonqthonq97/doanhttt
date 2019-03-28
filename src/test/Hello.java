package test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.zxing.WriterException;

import core.DBConnect;
import core.ResultAPI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;



@Path("/hello")
public class Hello {
	
	public Hello() {
	}

	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResultAPI sayHelloHTML(@HeaderParam("userName") String userName, @HeaderParam("pass")String Pass) throws SQLException {
		if(userName==null||Pass==null) return new ResultAPI(3, "Khong co thong tin dang nhap");
		if(!(userName.equals("hien")&&Pass.equals("hien"))) {
			return new ResultAPI(0,null);
		}
		
		DBConnect db = new DBConnect();
		if(db.isConnect()) {
			String sql = "select * from user";
			ArrayList<Object> listRs = new ArrayList<>();
			ResultSet rs = db.executeSQL(sql);
			while(rs.next()) {
				listRs.add(rs.getObject(2));
			}
			sql = "INSERT INTO `doanhttt`.`user` (`idUser`, `UserName`, `Password`, `QR`, `Role`) VALUES ('2', 'hien', 'kasdkajsdiudaj', '12', '2');";
			System.out.println(db.executeUpdate(sql));
			return new ResultAPI(1,listRs );
		} else {
			return new ResultAPI(0, "khong ket noi dc db");
		}
		
		/*ArrayList<Object> value = new ArrayList<>();
		Hien a1 = new Hien("hien",123);
		Hien a2 = new Hien("hien2",123);
		value.add(a1);
		value.add(a2);
		int abc[] = new int[3];
		abc[0]=1;abc[1]=2;abc[2]=4;
		value.add(abc);
		return new ResultAPI(1, value);*/
	}
	
	@GET
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public String sayaHelloHTML(@QueryParam("key") String key, @QueryParam("value") String value) {
		int value1;
		System.out.println("--" + key + " ---- " + value);
		try {
			value1 = Integer.parseInt(value);
		} catch(Exception e) {
			return "Add fail";
		}
		Hien.list.add(new Hien(key,value1));
		return "Add complete";
	}
	
	@GET
	@Path("/qr")
	@Produces(MediaType.TEXT_HTML)
	public String sayaHelloHTML(@QueryParam("text") String text) {
		String link = "C:/xampp/htdocs/img/100.png";
		try {
			QRCreater.generateQRCodeImage(text, 500, 500, link);
			System.out.println("DUng");
			return "<img src='http://13.70.25.1/img/100.png'>" ;
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}

