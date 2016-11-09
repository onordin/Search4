package search4.ejb;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.InternalServerErrorException;

import search4.daobeans.SubscriptionDAOBean;
import search4.daobeans.UserDAOBean;
import search4.ejb.interfaces.LocalUser;
import search4.ejb.passwordencryption.PBKDF2;
import search4.entities.DisplayUserEntity;
import search4.entities.SubscriptionEntity;
import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.DuplicateDataException;
import search4.exceptions.InvalidInputException;
import search4.entities.InfoPayload;
import search4.validators.EmailValidator;

@Stateless
public class UserEJB implements LocalUser, Serializable {

	@EJB
	private UserDAOBean userDAOBean;
    @EJB
    private SubscriptionDAOBean subscriptionDAOBean;
	
	public DisplayUserEntity createUser(UserEntity userEntity) throws DuplicateDataException, InternalServerErrorException{
		//Validation
		DisplayUserEntity displayUserEntity = null;
		userEntity.setEmail(userEntity.getEmail().toLowerCase()); //Keep all email addresses in lowercase always

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
		if (emailInDb(userEntity.getEmail())) {
			throw new DuplicateDataException("The email address already exists in the system");
		}
		else{
			try {
				String hashedPassword = PBKDF2.generatePasswordHash(userEntity.getPassword(), 666);
				userEntity.setPassword(hashedPassword);
				userDAOBean.createUser(userEntity);
				displayUserEntity = getUserWithEmail(userEntity.getEmail());
			} catch (NoSuchProviderException e) {
				throw new InternalServerErrorException("Something went wrong internally, please try again later!");
			} catch (NoSuchAlgorithmException e) {
				throw new InternalServerErrorException("Something went wrong internally, please try again later!");
			} catch (InvalidKeySpecException e) {
				throw new InternalServerErrorException("Something went wrong internally, please try again later!");
			}
		}
		return displayUserEntity;
	}

	private boolean emailInDb(String email) {
		return userDAOBean.userExist(email);
	}

	
	public DisplayUserEntity getUserToFrontend(String email, String password) {
		DisplayUserEntity displayUserEntity = getUser(email, password);
		if(displayUserEntity != null) {
			displayUserEntity.setPassword("");
		}
		return displayUserEntity;
	}
	
	

	public DisplayUserEntity getUser(String email, String password) {
		if(userDAOBean.userExist(email)) {
			UserEntity userEntity = userDAOBean.getUser(email);
			DisplayUserEntity displayUser = getDisplayUserFromDBEntity(userEntity);
			try {
				if (PBKDF2.validatePassword(password, displayUser.getPassword())) {
					return displayUser;
				}
				//TODO handle this better?
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return null;
		}
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
            current.setPassword("HIDDEN"); //TODO remove? remove password alltogehter?
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
			displayUser.setPassword("");
			return displayUser;
		}
		return null;
	}

	public DisplayUserEntity getUserByID(Integer id) throws DataNotFoundException{
		UserEntity userEntity = userDAOBean.getUser(id);
		DisplayUserEntity displayUserEntity = getDisplayUserFromDBEntity(userEntity);
		displayUserEntity.setPassword("");
		return displayUserEntity;
	}

	public List<DisplayUserEntity> getAllUsers() {
		List<DisplayUserEntity> result = new ArrayList<DisplayUserEntity>();
		List<UserEntity> userEntities = userDAOBean.getAll();
		for(UserEntity userEntity : userEntities) {
			DisplayUserEntity displayUserEntity = getDisplayUserFromDBEntity(userEntity);
			displayUserEntity.setPassword("");
			result.add(displayUserEntity);
		}
		return result;
	}

	//doesnt update other user details (done in another method)
	public InfoPayload changePassword(DisplayUserEntity activeUser) throws DuplicateDataException, InternalServerErrorException{

        InfoPayload infoPayload = new InfoPayload();
        DisplayUserEntity verifiedUser = getUser(activeUser.getEmail(), activeUser.getPassword());

        if(verifiedUser != null) {
            try {
                if (activeUser.getFirstPassword().length() < 4) {
                    throw new InvalidInputException("Password must be 4 or more characters.");
                }

                String hashedPassword = PBKDF2.generatePasswordHash(activeUser.getFirstPassword(), 666);
                UserEntity userEntity = new UserEntity();

                userEntity.setId(verifiedUser.getId());
                userEntity.setFirstName(verifiedUser.getFirstName());
                userEntity.setLastName(verifiedUser.getLastName());
                userEntity.setEmail(verifiedUser.getEmail());
                userEntity.setPassword(hashedPassword); 		//has now been hashed
                infoPayload = tryUpdateDatabaseAndInfoPayload(activeUser, userEntity, infoPayload);
            } catch (NoSuchProviderException e) {
                throw new InternalServerErrorException("Something went wrong internally, please try again later!");
            } catch (NoSuchAlgorithmException e) {
                throw new InternalServerErrorException("Something went wrong internally, please try again later!");
            } catch (InvalidKeySpecException e) {
                throw new InternalServerErrorException("Something went wrong internally, please try again later!");
            }
        }else {
            infoPayload.setUser_Message("User: " + activeUser.getEmail() + " has not been updated, please check username/password");
            infoPayload.setResultOK(false);
        }
        return infoPayload;
    }

	//doesnt update password (done in another method)
	public InfoPayload updateUserDetails(DisplayUserEntity activeUser) throws DuplicateDataException, InternalServerErrorException {

		InfoPayload infoPayload = new InfoPayload();
		DisplayUserEntity verifiedUser = getUser(activeUser.getEmail(), activeUser.getPassword());

		if(verifiedUser != null) {
			UserEntity userEntity = new UserEntity();
			userEntity.setId(verifiedUser.getId());

			//CHECK/VALIDATE NEW INPUTS
			EmailValidator emailValidator = new EmailValidator();

			if(activeUser.getFirstName() != null && activeUser.getFirstName() != "") {
				if (activeUser.getFirstName().length() > 255) {
					throw new InvalidInputException("First name must be less than or equal to 255 characters.");
				}
			}

			if(activeUser.getUpdatedEmail() != null && activeUser.getUpdatedEmail() != "") {
				if (activeUser.getUpdatedEmail().length() > 255 || !emailValidator.validateEmail(activeUser.getUpdatedEmail())) {
					throw new InvalidInputException("Email must be an email adress and less than or equal to 255 characters.");
				}
				if (emailInDb(activeUser.getUpdatedEmail()) && !activeUser.getUpdatedEmail().equalsIgnoreCase(activeUser.getEmail())) { //only check if newly entered emailaddress is already taken
					throw new DuplicateDataException("The email address already exists in the system");
				}
			}

			//CHECK NOT TO UPDATE TO EMPTY FIELDS (if using REST JSON)
			if(activeUser.getFirstName() != null && activeUser.getFirstName() != "") {
				userEntity.setFirstName(activeUser.getFirstName());
			}else {
				userEntity.setFirstName(verifiedUser.getFirstName());
			}

			if(activeUser.getLastName() != null && activeUser.getLastName() != "") {
			userEntity.setLastName(activeUser.getLastName());
			} else {
				userEntity.setLastName(verifiedUser.getLastName());
			}

			if(activeUser.getUpdatedEmail() != null && activeUser.getUpdatedEmail() != "") {
				userEntity.setEmail(activeUser.getUpdatedEmail());
			} else {
				userEntity.setEmail(verifiedUser.getEmail());
			}

			userEntity.setPassword(verifiedUser.getPassword());
			infoPayload = tryUpdateDatabaseAndInfoPayload(activeUser, userEntity, infoPayload);
		} else {
			infoPayload.setUser_Message("User: " + activeUser.getEmail() + " has not been updated, please check username/password");
			infoPayload.setResultOK(false);
		}
		return infoPayload;
	}

	
	private InfoPayload tryUpdateDatabaseAndInfoPayload(DisplayUserEntity displayUserEntity, UserEntity userEntity, InfoPayload infoPayload) {
		if(userDAOBean.updateUser(userEntity)) {
			infoPayload.setUser_Message("User: " + displayUserEntity.getEmail() + " has been updated");
			infoPayload.setResultOK(true);
			infoPayload.setInternal_Message(Integer.toString(userEntity.getId()));
		} else {
			infoPayload.setUser_Message("User: " + displayUserEntity.getEmail() + " has not been updated");
			infoPayload.setResultOK(false);
		}
		return infoPayload;
	}

    public InfoPayload deleteUser(Integer id) {
        InfoPayload infoPayload = new InfoPayload();

        if(userDAOBean.userExist(id)) {
            System.out.println("User exist!!! with Id = " +id);
            if(userDAOBean.deleteUser(id)) {
                infoPayload.setUser_Message("User number: " + id + " has been deleted");
                infoPayload.setResultOK(true);
            } else {
                infoPayload.setUser_Message("User number: " + id + " exists but has not been deleted");
                infoPayload.setResultOK(false);
            }
        } else {
            infoPayload.setUser_Message("User does not exist with id = " + id);
            infoPayload.setResultOK(false);
        }
        return infoPayload;
    }
}

