package search4.daobeans;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import search4.entities.MovieEntity;

@Stateful
public class SearchDatabaseDAOBean {

    @PersistenceContext
    private EntityManager entityManager;

	public MovieEntity findTMDbIdFromTitle(String title) {
		Integer TMDbId = null;
		MovieEntity movie = null;
		
		try{
		TypedQuery<MovieEntity> query = entityManager.createQuery(
		        "SELECT c FROM MovieEntity c WHERE c.title = '" + title + "'",
		        MovieEntity.class);
	 	movie = query.getSingleResult();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	 	return movie;
	 	
	}

    
}
