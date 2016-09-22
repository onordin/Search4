package search4.ejb;

import javax.ejb.EJB;

import search4.daobeans.UserDAOBean;
import search4.ejb.interfaces.LocalUser;
import search4.entities.UserEntity;

public class UserEJB implements LocalUser {

	@EJB
	private UserDAOBean userDAOBean;
	
	public void createUser(UserEntity userEntity) {
		userDAOBean.createUser(userEntity);
	}

	
	public UserEntity getUser() {
		// TODO Get user
		return null;
	}

}
