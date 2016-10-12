package search4.backingbeans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.InternalServerErrorException;

import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplayUserEntity;
import search4.entities.UserEntity;
import search4.exceptions.DuplicateDataException;
import search4.exceptions.InvalidInputException;

@Named(value="userBean")
@SessionScoped
public class UserBean implements Serializable{

	private static final long serialVersionUID = -4702186913617620140L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String message;
	private String userIsLoggedIn;
	private String passwordReset;
	private String firstNewPassword;
	private String secondNewPassword;
	

	private DisplayUserEntity displayUserEntity;
	
	@EJB
	LocalUser userEJB;
	
	public String createUser(){
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setEmail(email);
		userEntity.setPassword(password);
		try {
			userEJB.createUser(userEntity);
			message = "New user with email: " + email + " created";
			return "full_startpage";
		} catch (DuplicateDataException dde) {
			message = dde.getMessage();
			return "full_startpage";
		} catch (InternalServerErrorException isee) {
			message = isee.getMessage();
			return "full_startpage";
		} catch (InvalidInputException iie) {
			message = iie.getMessage();
			return "full_startpage";
		}
		catch (Exception e) {
			message = "Unknown Error";
			return "full_startpage";
		}
	}
	
	public String loginUser(){
		displayUserEntity = userEJB.getUser(email, password);
		if (displayUserEntity == null) {
			message = "Email or Password Wrong!";
			userIsLoggedIn = null;
			return "login";
		}
		message = "Login Successfull";
		userIsLoggedIn = "user is now logged in";
		//Wiping these just to be sure
		firstName = "";
		lastName = "";
		email = "";
		password = "";
		return "full_startpage";
	}
	
	public String logOffUser() {
		message = "You logged out.";
		userIsLoggedIn = null;
		firstName = "";
		lastName = "";
		email = "";
		password = "";
		return "full_startpage";
		
	}
	
	public String forgotPassword() {
		displayUserEntity = userEJB.getUserWithEmail(email);
		
		if(displayUserEntity == null) {
			passwordReset = "Email doesn't exist";
			email = "";
			return "forgot_password";
		}
		// generate new random password
		// update db
		// send email
		passwordReset = "Instrctions sent to " +email;
		email = "";
		return "forgot_password";
	}
	
	
	public void changePassword() {
		// kolla om lösenordet matcher email
		System.out.println("email = " +(displayUserEntity = userEJB.getUserWithEmail(email)));
		System.out.println("old = " +password);
		System.out.println("new 1 = " +firstNewPassword);
		System.out.println("new 2 = " +secondNewPassword);
		
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

	public DisplayUserEntity getDisplayUserEntity() {
		return displayUserEntity;
	}

	public void setDisplayUserEntity(DisplayUserEntity displayUserEntity) {
		this.displayUserEntity = displayUserEntity;
	}

	public String getMessage() {
		return message;
	}
	
	public String viewProfile() {
		return "full_profile";
	}

	public String getPasswordReset() {
		return passwordReset;
	}

	public void setPasswordReset(String passwordReset) {
		this.passwordReset = passwordReset;
	}
	
	public String getUserIsLoggedIn() {
		return userIsLoggedIn;
	}

	public void setUserIsLoggedIn(String userIsLoggedIn) {
		this.userIsLoggedIn = userIsLoggedIn;
	}

	public String getFirstNewPassword() {
		return firstNewPassword;
	}

	public void setFirstNewPassword(String firstNewPassword) {
		this.firstNewPassword = firstNewPassword;
	}

	public String getSecondNewPassword() {
		return secondNewPassword;
	}

	public void setSecondNewPassword(String secondNewPassword) {
		this.secondNewPassword = secondNewPassword;
	}
	
	
}
