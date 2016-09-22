package search4.daobeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import search4.entities.UserEntity;

@Stateless
public class UserDAOBean {

	@PersistenceContext
	private EntityManager entityManager;
	
	public boolean createUser(UserEntity userEntity){
		entityManager.merge(userEntity);
		return true;
	}
	
	public UserEntity getUser(Integer userId){
		return null;
	}
}
