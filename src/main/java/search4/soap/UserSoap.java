package search4.soap;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
		try {
			return userEJB.getUserToFrontend(email, password);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
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
