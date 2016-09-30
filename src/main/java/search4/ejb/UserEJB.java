package search4.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.InternalServerErrorException;

import search4.daobeans.UserDAOBean;
import search4.ejb.interfaces.LocalUser;
import search4.ejb.passwordencrytion.PBKDF2;
import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.DuplicateDataException;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

@Stateless
public class UserEJB implements LocalUser {

	@EJB
	private UserDAOBean userDAOBean;
	
	public void createUser(UserEntity userEntity) throws DuplicateDataException, InternalServerErrorException{
		userEntity.setEmail(userEntity.getEmail().toLowerCase()); //Keep all email addresses in lowercase always //TODO right place to do this?
		if (emailInDb(userEntity.getEmail())) {
			throw new DuplicateDataException("The email address already exists in the system");
		}
		else {
			try {
				String hashedPassword = PBKDF2.generatePasswordHash(userEntity.getPassword(), 666);
				userEntity.setPassword(hashedPassword);
				userDAOBean.createUser(userEntity);
			} catch (NoSuchProviderException e) {
				throw new InternalServerErrorException("Something went wrong internally, please try again later!");
			} catch (NoSuchAlgorithmException e) {
				throw new InternalServerErrorException("Something went wrong internally, please try again later!");
			} catch (InvalidKeySpecException e) {
				throw new InternalServerErrorException("Something went wrong internally, please try again later!");
			}
		}
	}

	private boolean emailInDb(String email) {
		return userDAOBean.userExist(email);
	}

	
	public UserEntity getUser(String email,String password) {
		UserEntity userEntity = userDAOBean.getUser(email);
		checkPassword(userEntity, password);
		return userEntity;
	}

	//TODO make more fancy method for login
	private void checkPassword(UserEntity userEntity, String password) {
		if (userEntity.getPassword().equals(password)) {
			System.out.println("CHECKPASSWORD: " + userEntity.getFirstName() + " logged in!");
		}else{
			
		}
		
	}

}
