package search4.backingbeans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.inject.Named;

import search4.ejb.interfaces.LocalUser;
import search4.entities.UserEntity;

@Named(value="userBean")
public class UserBean implements Serializable{

	private static final long serialVersionUID = -4702186913617620140L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	@EJB
	LocalUser userEJB;
	
	public String createUser(){
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setEmail(email);
		userEntity.setPassword(password);
		userEJB.createUser(userEntity);
		return "";
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
