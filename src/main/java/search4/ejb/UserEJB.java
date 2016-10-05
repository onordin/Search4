package search4.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.InternalServerErrorException;

import search4.daobeans.UserDAOBean;
import search4.ejb.interfaces.LocalUser;
import search4.ejb.passwordencrytion.PBKDF2;
import search4.entities.DisplayUserEntity;
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

	
	public DisplayUserEntity getUser(String email,String password) {
		UserEntity userEntity = userDAOBean.getUser(email);
		DisplayUserEntity displayUser = getDisplayUserFromDBEntity(userEntity);
		try {
			if (PBKDF2.validatePassword(password, displayUser.getPassword())) {
				displayUser.setPassword("HIDDEN");
				return displayUser;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	private DisplayUserEntity getDisplayUserFromDBEntity(UserEntity userEntity) {
		DisplayUserEntity displayUser = new DisplayUserEntity();
		displayUser.setId(userEntity.getId());
		displayUser.setEmail(userEntity.getEmail());
		displayUser.setPassword(userEntity.getPassword());
		displayUser.setFirstName(userEntity.getFirstName());
		displayUser.setLastName(userEntity.getLastName());
		return displayUser;
	}
}
