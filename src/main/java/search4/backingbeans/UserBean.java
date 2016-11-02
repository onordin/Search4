package search4.backingbeans;

import java.io.IOException;
import java.io.Serializable;

import java.util.Map;

import java.util.Arrays;


import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.InternalServerErrorException;

import search4.ejb.UserEJB;
import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplayUserEntity;
import search4.entities.UserEntity;
import search4.exceptions.DuplicateDataException;
import search4.exceptions.InvalidInputException;
import search4.resources.entities.InfoPayload;

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
	
	private String viewId;
	private Integer id;

	private DisplayUserEntity displayUserEntity;
	
	@EJB
	private LocalUser userEJB;
	
	public void createUser(){
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setEmail(email);
		userEntity.setPassword(password);
		try {
			userEJB.createUser(userEntity);
			message = "New user with email: " + email + " created";
			String returnView = viewId.replace("/", "");
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect(returnView+"?id="+id);
			
		} catch (DuplicateDataException dde) {
			message = dde.getMessage();
			//return "full_startpage";
		} catch (InternalServerErrorException isee) {
			message = isee.getMessage();
			//return "full_startpage";
		} catch (InvalidInputException iie) {
			message = iie.getMessage();
			//return "full_startpage";
		}
		catch (Exception e) {
			message = "Unknown Error";
			//return "full_startpage";
		}
	}
	

	public void loginUser(){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		System.out.println("HHHHHEEEEEEJJJJJJ");
		try {
			displayUserEntity = userEJB.getUser(email, password);
			String returnView = viewId.replace("/", "");
			if (displayUserEntity == null) {
				displayUserEntity.setPassword("");
				message = "Email or Password Wrong!";
				userIsLoggedIn = null;
				System.out.println("Log in error, userIsLoggedIn: " + userIsLoggedIn );
				externalContext.redirect(externalContext.getRequestContextPath() + viewId+"?id="+id);
			}else{
			message = "Login Successfull";
			userIsLoggedIn = "user is now logged in";
			//Wiping these just to be sure
			firstName = "";
			lastName = "";
			email = "";
			password = "";
			message = "";
			externalContext.redirect(externalContext.getRequestContextPath() + viewId+"?id="+id);
			}
		} catch (IOException e) {
			message = e.getMessage();
		}
	}
	
	public String deleteUser() {
		
		InfoPayload resultFromDelete = userEJB.deleteUser(displayUserEntity.getId());

		if(resultFromDelete.isResultOK()) {
			System.out.println(resultFromDelete.getUser_Message());
			logOffUser();
		} else {
			System.out.println(resultFromDelete.getUser_Message());
		}
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
		DisplayUserEntity activeUser = userEJB.getUser(displayUserEntity.getEmail(), password);	//checks correct old password
		if(activeUser != null) {
			if(firstPassword.equals(secondPassword)) {
				if(firstPassword.matches(pattern)) {
					activeUser.setPassword(password); //to check old password again in EJB
					activeUser.setFirstPassword(firstPassword);
					activeUser.setSecondPassword(secondPassword);
					userEJB.changePassword(activeUser);						
					activeUser.setFirstPassword("");
					activeUser.setSecondPassword("");
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

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
