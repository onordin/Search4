package search4.soap;

import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;

import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplayUserEntity;
import search4.entities.InfoPayload;
import search4.entities.UserEntity;

@WebService(serviceName="userSoap")
public class UserSoap {

	@EJB
	private LocalUser userEJB;
	
	public List<DisplayUserEntity> getAllUsers() {
		return userEJB.getAllUsers();
	}
	
	
	public DisplayUserEntity getUser(String email, String password) {
		return userEJB.getUserToFrontend(email, password);
	}
	

	public DisplayUserEntity createUser(String firstName, String lastName, String email, String password) {
		UserEntity userEntity = new UserEntity();
		DisplayUserEntity displayUserEntity = null;
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setEmail(email);
		userEntity.setPassword(password);
		try{
			displayUserEntity = userEJB.createUser(userEntity);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return displayUserEntity;
	}
	
	
	public InfoPayload deleteUser(Integer id) {
		return userEJB.deleteUser(id);
	}
	
}
