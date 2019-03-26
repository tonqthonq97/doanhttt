package test;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "Hien")
@XmlAccessorType(XmlAccessType.FIELD)
public class Hien{
	public static ArrayList<Hien> list = new ArrayList<>();
	private String key;
	private int value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Hien() {
		
	}
	public Hien(String key, int value) {
		this.key=key;
		this.value=value;
	}
}