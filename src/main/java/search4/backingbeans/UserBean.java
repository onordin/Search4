package search4.backingbeans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import search4.ejb.interfaces.LocalUser;
import search4.entities.UserEntity;

@Named(value="userBean")
@SessionScoped
public class UserBean implements Serializable{

	private static final long serialVersionUID = -4702186913617620140L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String message;
	
	private UserEntity userEntity;
	
	@EJB
	LocalUser userEJB;
	
	public String createUser(){
		userEntity = new UserEntity();
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setEmail(email); //TODO check if email is already in db.
		userEntity.setPassword(password); // TODO password security
		userEJB.createUser(userEntity);
		message = "New user with email: " + email + "created";
		return "login";
	}
	
	public String loginUser(){
		userEntity= userEJB.getUser(email,password);//TODO sercurity und so weiter
		return "testUserResult";
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

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
	
	
	
	
}
