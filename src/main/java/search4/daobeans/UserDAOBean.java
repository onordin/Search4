package search4.daobeans;


import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.DuplicateDataException;

import java.io.Serializable;
import java.util.List;

@Stateful
public class UserDAOBean implements Serializable{

	@PersistenceContext
	private EntityManager entityManager;
	
	public boolean createUser(UserEntity userEntity){
		try {
			entityManager.merge(userEntity);
			return true;
		} catch (Exception e) {
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

	public UserEntity getUser(String email) throws DataNotFoundException {
		try {
			return (UserEntity) entityManager.createNamedQuery("UserEntity.getUserByEmail")
					.setParameter("email", email)
					.getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException("No such email ("+email+") in database.");
		}
	}

	public UserEntity getUser(Integer id) throws DataNotFoundException {
		try {
			return (UserEntity) entityManager.createNamedQuery("UserEntity.getUserById")
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException("No such id ("+id+") in database.");
		}
	}

	public boolean changePassword(UserEntity userEntity) {
		try {
			entityManager.merge(userEntity);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean userExist(int id) {
		try {
			UserEntity userEntity = (UserEntity) entityManager.createNamedQuery("UserEntity.getUserById").setParameter("id", id).getSingleResult();
			return true;
		} catch (NoResultException nre) {
			return false;
		}
	}

	public boolean deleteUser(int id) {
		try {
			entityManager.createNamedQuery("ProviderEntity.deleteUser").setParameter("userId", id).executeUpdate();
			entityManager.createNamedQuery("SubscriptionEntity.deleteUser").setParameter("userId", id).executeUpdate();
			entityManager.createNamedQuery("UserEntity.deleteUserById").setParameter("id", id).executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}

	}
}
