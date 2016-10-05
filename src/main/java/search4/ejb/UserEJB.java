package search4.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import search4.daobeans.UserDAOBean;
import search4.ejb.interfaces.LocalUser;
import search4.entities.UserEntity;

@Stateless
public class UserEJB implements LocalUser {
	@EJB
	private UserDAOBean userDAOBean;
	
	public void createUser(UserEntity userEntity) {
		userDAOBean.createUser(userEntity);
	}
	
	public UserEntity getUser(String email,String password) {
		UserEntity userEntity = userDAOBean.getUser(email);
		checkPassword(userEntity,password); 
		return userEntity;
	}
	//TODO make more fancy method for login
	private void checkPassword(UserEntity userEntity, String password) {
		if (userEntity.getPassword().equals(password)) {
			System.out.println("CHECKPASSWORD: " + userEntity.getFirstName() + " logged in!");
		}else{
			
		}
		
	}
}
