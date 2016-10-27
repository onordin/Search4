package search4.ejb.interfaces;

import javax.ejb.Local;

import search4.entities.DisplayUserEntity;
import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;

import java.util.List;

@Local
public interface LocalUser {

	void createUser(UserEntity userEntity);
	
	DisplayUserEntity getUser(String email, String password);

	DisplayUserEntity getUserWithEmail(String email);
	
	DisplayUserEntity getUserByID(Integer id) throws DataNotFoundException;

	void changePassword(DisplayUserEntity activeUser);

	boolean deleteUser(int id);

	List<DisplayUserEntity> getDisplayUsersSubscribedTo(Integer movieId);

	List<DisplayUserEntity> getAllUsers();

}
