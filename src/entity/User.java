package entity;

public class User {
	private String idUser;
	private String userName;
	private String role;
	public User() {
	}
	
	public User(String idUser, String userName, String role) {
		super();
		this.idUser = idUser;
		this.userName = userName;
		this.role = role;
	}

	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	

}
