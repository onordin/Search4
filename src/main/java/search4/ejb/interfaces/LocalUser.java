package search4.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import search4.entities.DisplayUserEntity;
import search4.entities.InfoPayload;
import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.DuplicateDataException;

@Local
public interface LocalUser {

	DisplayUserEntity createUser(UserEntity userEntity);
	
	DisplayUserEntity getUserToFrontend(String email, String password);
	
	DisplayUserEntity getUser(String email, String password);

	DisplayUserEntity getUserWithEmail(String email);
	
	DisplayUserEntity getUserByID(Integer id);

	InfoPayload changePassword(DisplayUserEntity activeUser);
	
	InfoPayload updateUserDetails(DisplayUserEntity activeUser);

	InfoPayload deleteUser(Integer id) ;

	List<DisplayUserEntity> getDisplayUsersSubscribedTo(Integer movieId);

	List<DisplayUserEntity> getAllUsers();

}
