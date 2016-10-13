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
	private String firstPassword;
	private String secondPassword;
	

	private DisplayUserEntity displayUserEntity;
	
	@EJB
	private LocalUser userEJB;
	
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
			return "full_startpage";
		}
		message = "Login Successfull";
		userIsLoggedIn = "user is now logged in";
		//Wiping these just to be sure
		firstName = "";
		lastName = "";
		email = "";
		password = "";
		message = "";
		return "full_startpage";
	}
	
	public String logOffUser() {
		displayUserEntity = null;
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
		return "full_forgot_password";
	}
	
	public String changePassword() {
		String pattern = "((?=.*[a-z]).{6,})";
		// 1. kolla om gamla lösenordet är rätt
		// 2. kolla om first o second är samma
		// 3. uppdatera db med nya lösen
		DisplayUserEntity activeUser = userEJB.getUser(displayUserEntity.getEmail(), password);
		if(activeUser != null) {
			if(firstPassword.equals(secondPassword)) {
				if(firstPassword.matches(pattern)) {
					activeUser.setPassword(firstPassword);
					System.out.println("USerBEAN!!!!!!!!!!!!!!!!!!!!");
					System.out.println("ID = " +activeUser.getId());
					System.out.println("First name = " +activeUser.getFirstName());
					System.out.println("Last name = " +activeUser.getLastName());
					System.out.println("Email = " +activeUser.getEmail());
					System.out.println("Password = " +activeUser.getPassword());
					userEJB.changePassword(activeUser);
					message = "New password saved!";
				} else {
					message = "New password needs to be at least 6 charecters long (plus en liten bokstav...)";
				}
			} else {
				message = "Both new passwords have to match. New password was not changed.";
			}
		} else {
			message = "Old password is wrong. New password was not changed.";
		}
		password = "";
		firstPassword = "";
		secondPassword = "";
		return "full_profile";
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

	public String getFirstPassword() {
		return firstPassword;
	}

	public void setFirstPassword(String firstPassword) {
		this.firstPassword = firstPassword;
	}

	public String getSecondPassword() {
		return secondPassword;
	}

	public void setSecondPassword(String secondPassword) {
		this.secondPassword = secondPassword;
	}

	
	
}
