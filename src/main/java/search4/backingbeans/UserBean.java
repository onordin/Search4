package search4.backingbeans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.InternalServerErrorException;

import search4.ejb.interfaces.LocalEmail;
import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplayUserEntity;
import search4.entities.InfoPayload;
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
	private String registerMessage;
	private String userIsLoggedIn;
	private String passwordReset;
	private String firstPassword;
	private String secondPassword;
	
	private String viewId;
	private Integer id;
	

	private DisplayUserEntity displayUserEntity;
	
	@EJB
	private LocalUser userEJB;
	@EJB
	private LocalEmail emailEJB;
	
	public void postInit() {
		passwordReset = "";
	}
	
	public String createUser(){
		String pattern = "((?=.*[A-Za-z]).{6,})";
		UserEntity userEntity = new UserEntity();
		
		if(firstPassword.equals(secondPassword)) {
			if(firstPassword.matches(pattern)) {
				userEntity.setPassword(firstPassword);
			} else {
				registerMessage = "Password needs to be 6-50 characters";
				return "full_register";
			}
		} else {
			registerMessage = "Both new passwords have to match";
			return "full_register";
		}

		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setEmail(email);
		
		try {
			userEJB.createUser(userEntity);
			firstName = "";
			lastName = "";
			email = "";
			password = "";
			message = "New user with email: " + email + " created";
			return "full_startpage";
		} catch (DuplicateDataException dde) {
			registerMessage = dde.getMessage();
			return "";
		} catch (InternalServerErrorException isee) {
			registerMessage = isee.getMessage();
			return "";
		} catch (InvalidInputException iie) {
			registerMessage = iie.getMessage();
			return "";
		}
		catch (Exception e) {
			message = "Unknown Error";
			return "full_startpage";
		}
	}
	

	public void loginUser(){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		try {
			displayUserEntity = userEJB.getUserToFrontend(email, password);
			String returnView = viewId.replace("/", "");
			if (displayUserEntity == null) {
				message = "Email or Password Wrong!";
				userIsLoggedIn = null;
				externalContext.redirect(externalContext.getRequestContextPath() + viewId+"?id="+id);
			}else{
			message = "Login Successfullll";
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
		} catch (NoSuchAlgorithmException e) {
			message = e.getMessage();
		} catch (InvalidKeySpecException e) {
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
		
		DisplayUserEntity activeUser = userEJB.getUserWithEmail(email);
		if(activeUser == null) {
			passwordReset = "Email doesn't exist";
			email = "";
			return "full_password_fail";
		}
		String randomPassword = GenerateRandomPassword();
		activeUser.setFirstPassword(randomPassword);
		activeUser.setSecondPassword(randomPassword);
		userEJB.changePasswordByEmail(activeUser);
		String mail = activeUser.getEmail();
		System.out.println("New random password = " +randomPassword);
		emailEJB.sendForgotPasswordMail(mail, randomPassword);
		passwordReset = "";
		email = "";
		
		return "full_password_sent";
	}
	

	private String GenerateRandomPassword() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	  
	
	public String changePassword() {
		String pattern = "((?=.*[A-Za-z]).{6,})";
		// 1. kolla om gamla lösenordet är rätt
		// 2. kolla om first o second är samma
		// 3. uppdatera db med nya lösen
		DisplayUserEntity activeUser = null;
		String errorMessage = "";
		try {
			activeUser = userEJB.getUserToFrontend(displayUserEntity.getEmail(), password);
		} catch (NoSuchAlgorithmException nsae) {
			errorMessage = nsae.getMessage();
			message = errorMessage;
		}	//checks correct old password
		catch (InvalidKeySpecException ikse) {
			errorMessage = ikse.getMessage();
			message = errorMessage;
		}
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
					message = "New password needs to be at least 6 -50 characters";
				}
			} else {
				message = "Both new passwords have to match. New password was not changed.";
			}
		} else {
			if(errorMessage == "") {	//otherwise it has also set message-variable above because of caught exception...  
				message = "Old password is wrong. New password was not changed.";
			}
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


	public String getRegisterMessage() {
		return registerMessage;
	}


	public void setRegisterMessage(String registerMessage) {
		this.registerMessage = registerMessage;
	}


	public void setMessage(String message) {
		this.message = message;
	}

}
