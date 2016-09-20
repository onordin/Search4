package search4.ejb.interfaces;

import java.util.List;
import javax.ejb.Local;

import search4.entities.MovieEntity;

@Local
public interface LocalSearch {

	List<MovieEntity> search(String parameter);
	
	List<MovieEntity> searchOrderByDateAsc(String parameter);
	
	List<MovieEntity> searchOrderByDateDesc(String parameter);
	
	List<MovieEntity> searchOrderByTitleAsc(String parameter);
	
	List<MovieEntity> searchOrderByTitleDesc(String parameter);
}
