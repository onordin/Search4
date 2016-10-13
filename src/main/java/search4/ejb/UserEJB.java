package search4.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.InternalServerErrorException;

import search4.daobeans.SubscriptionDAOBean;
import search4.daobeans.UserDAOBean;
import search4.ejb.interfaces.LocalUser;
import search4.ejb.passwordencrytion.PBKDF2;
import search4.entities.DisplayUserEntity;
import search4.entities.SubscriptionEntity;
import search4.entities.UserEntity;
import search4.exceptions.DuplicateDataException;
import search4.exceptions.InvalidInputException;
import search4.validators.EmailValidator;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserEJB implements LocalUser {

	@EJB
	private UserDAOBean userDAOBean;
    @EJB
    private SubscriptionDAOBean subscriptionDAOBean;
	
	public void createUser(UserEntity userEntity) throws DuplicateDataException, InternalServerErrorException{
		//Validation
		EmailValidator emailValidator = new EmailValidator();
		if (userEntity.getFirstName().length() > 255) {
			throw new InvalidInputException("First name must be less than or equal to 255 characters.");
		}
		if (userEntity.getEmail().length() > 255 || !emailValidator.validateEmail(userEntity.getEmail())) {
			throw new InvalidInputException("Email must be an email adress and less than or equal to 255 characters.");
		}
		if (userEntity.getPassword().length() < 4) {
			throw new InvalidInputException("Password must be 4 or more characters.");
		}
 		userEntity.setEmail(userEntity.getEmail().toLowerCase()); //Keep all email addresses in lowercase always //TODO right place to do this? ask teacher
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

	
	public DisplayUserEntity getUser(String email, String password) {
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

	public List<DisplayUserEntity> getDisplayUsersSubscribedTo(Integer movieId) {
        List<UserEntity> userEntities = getUsersSubscribedTo(movieId);
        List<DisplayUserEntity> displayUserEntities = new ArrayList<DisplayUserEntity>();
        DisplayUserEntity current;
        for (UserEntity userEntity : userEntities) {
            current = getDisplayUserFromDBEntity(userEntity);
            current.setPassword("HIDDEN");
            displayUserEntities.add(current);
        }
        return displayUserEntities;
	}

	private List<UserEntity> getUsersSubscribedTo(Integer movieId) {
        List<SubscriptionEntity> subscriptionEntities = subscriptionDAOBean.getUsersSubscribedTo(movieId);
        List<UserEntity> userEntities = new ArrayList<UserEntity>();
        for (SubscriptionEntity subscriptionEntity : subscriptionEntities) {
            userEntities.add(userDAOBean.getUser(subscriptionEntity.getId()));
        }
        return userEntities;
    }

	public DisplayUserEntity getUserWithEmail(String email) {
		if(emailInDb(email)) {
			UserEntity userEntity = userDAOBean.getUser(email);
			DisplayUserEntity displayUser = getDisplayUserFromDBEntity(userEntity);
			return displayUser;
		}
		return null;
	}
}
