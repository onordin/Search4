package search4.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;
import javax.ws.rs.InternalServerErrorException;

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
	
	DisplayUserEntity getUserByID(Integer id) throws DataNotFoundException;

	InfoPayload changePassword(DisplayUserEntity activeUser) throws DuplicateDataException, InternalServerErrorException;
	
	InfoPayload updateUserDetails(DisplayUserEntity activeUser) throws DuplicateDataException, InternalServerErrorException;

	InfoPayload deleteUser(Integer id) ;

	List<DisplayUserEntity> getDisplayUsersSubscribedTo(Integer movieId);

	List<DisplayUserEntity> getAllUsers();

}
