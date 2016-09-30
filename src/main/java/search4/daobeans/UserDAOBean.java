package search4.daobeans;


import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.DuplicateDataException;

@Stateful
public class UserDAOBean {

	@PersistenceContext
	private EntityManager entityManager;
	
	public boolean createUser(UserEntity userEntity){
		System.out.println("in create user");
		try {
			System.out.println("Trying to create user...");
			entityManager.merge(userEntity);
			System.out.println("It worked");
			return true;
		} catch (Exception e) {
			System.err.println("1234: " + e);
			return false;
		}
	}

	public boolean userExist(String email) {
		try {
			UserEntity userEntity = (UserEntity) entityManager.createNamedQuery("UserEntity.getUserByEmail").setParameter("email", email).getSingleResult();
			return true;
		} catch (NoResultException nre) {
			return false;
		}
	}

	//TODO this somehow breaks something?
	public UserEntity getUser(String email) throws DataNotFoundException {
		try {
			return (UserEntity) entityManager.createNamedQuery("UserEntity.getUserByEmail").setParameter("email", email).getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException("No such email ("+email+") in database.");
		}
	}
}
