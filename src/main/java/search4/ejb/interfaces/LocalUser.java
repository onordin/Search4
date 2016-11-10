package search4.ejb.interfaces;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

	DisplayUserEntity createUser(UserEntity userEntity) throws DuplicateDataException, InternalServerErrorException, NoSuchAlgorithmException, InvalidKeySpecException;
	
	DisplayUserEntity getUserToFrontend(String email, String password)throws NoSuchAlgorithmException, InvalidKeySpecException;
	
	DisplayUserEntity getUser(String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

	DisplayUserEntity getUserWithEmail(String email);
	
	DisplayUserEntity getUserByID(Integer id);

	InfoPayload changePassword(DisplayUserEntity activeUser);
	
	InfoPayload updateUserDetails(DisplayUserEntity activeUser);

	InfoPayload deleteUser(Integer id);

	List<DisplayUserEntity> getDisplayUsersSubscribedTo(Integer movieId);

	List<DisplayUserEntity> getAllUsers();

}
