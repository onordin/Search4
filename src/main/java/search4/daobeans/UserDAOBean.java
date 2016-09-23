package search4.daobeans;


import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;

@Stateful
public class UserDAOBean {

	@PersistenceContext
	private EntityManager entityManager;
	
	public boolean createUser(UserEntity userEntity){
		entityManager.merge(userEntity);
		return true;
	}
	
	public UserEntity getUser(String email) {
		return (UserEntity) entityManager.createNamedQuery("UserEntity.getUserByEmail").setParameter("email", email).getSingleResult();
	}
}
