package search4.ejb.interfaces;

import java.util.List;
import javax.ejb.Local;

import search4.entities.MovieEntity;

@Local
public interface LocalSearch {
	
	List<MovieEntity> searchOrderByDateAsc(String parameter, Integer limit);
	
	List<MovieEntity> searchOrderByDateDesc(String parameter, Integer limit);
	
	List<MovieEntity> searchOrderByTitleAsc(String parameter, Integer limit);
	
	List<MovieEntity> searchOrderByTitleDesc(String parameter, Integer limit);
}
