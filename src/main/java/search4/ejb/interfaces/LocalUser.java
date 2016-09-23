package search4.ejb.interfaces;

import javax.ejb.Local;

import search4.entities.UserEntity;

@Local
public interface LocalUser {

	void createUser(UserEntity userEntity);
	
	UserEntity getUser(String email,String password);
}
