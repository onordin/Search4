package search4.ejb.interfaces;

import javax.ejb.Local;

import search4.entities.MovieEntity;

@Local
public interface LocalUpdateDatabase {
	
	MovieEntity getMovieFromTMDB(int id);
	
	boolean updateDatabase() throws Exception;
	
	Integer getTMDBLimit(int start);
	
	Integer getLastTMDBIdFromDB() throws Exception;
}
