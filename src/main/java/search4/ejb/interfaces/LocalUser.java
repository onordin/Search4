package search4.ejb.interfaces;

import javax.ejb.Local;

import search4.entities.DisplayUserEntity;
import search4.entities.UserEntity;

import java.util.List;

@Local
public interface LocalUser {

	void createUser(UserEntity userEntity);
	
	DisplayUserEntity getUser(String email, String password);

	DisplayUserEntity getUserWithEmail(String email);

	List<DisplayUserEntity> getDisplayUsersSubscribedTo(Integer movieId);


}
