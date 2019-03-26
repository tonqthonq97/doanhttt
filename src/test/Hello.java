package test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;



@Path("/hello")
public class Hello {

	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Hien> sayHelloHTML() {
		return Hien.list;
	}
	
	@GET
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public String sayHelloHTML(@QueryParam("key") String key, @QueryParam("value") String value) {
		int value1;
		try {
			value1 = Integer.parseInt(value);
		} catch(Exception e) {
			return "Add fail";
		}
		Hien.list.add(new Hien(key,value1));
		return "Add complete";
	}
}

