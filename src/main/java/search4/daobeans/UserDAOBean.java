package search4.daobeans;


import javax.ejb.EJBContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;

import java.io.Serializable;
import java.util.List;

@Stateful
public class UserDAOBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9147207018580784695L;
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void createUser(UserEntity userEntity){
		try {
			entityManager.merge(userEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public List<UserEntity> getAll() {
		return entityManager.createNamedQuery("UserEntity.getAll")
                .getResultList();
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

	public boolean updateUser(UserEntity userEntity) {
		try {
			entityManager.merge(userEntity);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean userExist(Integer id) {
		try {
			UserEntity userEntity = (UserEntity) entityManager.createNamedQuery("UserEntity.getUserById").setParameter("id", id).getSingleResult();
			return true;
		} catch (NoResultException nre) {
			return false;
		}
	}

	public boolean deleteUser(Integer id) {
		try {
			entityManager.createNamedQuery("SubscriptionEntity.deleteUser").setParameter("userId", id).executeUpdate();
			entityManager.createNamedQuery("UserEntity.deleteUserById").setParameter("id", id).executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}

	}
}
