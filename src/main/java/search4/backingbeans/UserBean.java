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
	
	private DisplayUserEntity displayUserEntity;
	
	@EJB
	LocalUser userEJB;
	
	public String createUser(){
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setEmail(email); //TODO check if email is already in db.
		userEntity.setPassword(password); // TODO password security
		try {
			userEJB.createUser(userEntity);
			message = "New user with email: " + email + " created";
			return "full_startpage";
		} catch (DuplicateDataException dde) {
			message = dde.getMessage(); //TODO is this the right way to do it?
			return "full_startpage"; //TODO create error page
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
			return "login"; //TODO display message
		}
		message = "Login Successfull";
		//Wiping these just to be sure //TODO this the right way to go about this?
		firstName = "";
		lastName = "";
		email = "";
		password = "";
		return "full_startpage"; //TODO create page
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
}
