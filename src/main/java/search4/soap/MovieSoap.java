package search4.soap;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;

import search4.ejb.interfaces.LocalDisplayMovie;
import search4.ejb.interfaces.LocalSearch;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;

@WebService(serviceName="movieSoap")
public class MovieSoap {
	
	@EJB
	private LocalDisplayMovie displayMovieEJB;
	
	@EJB
	private LocalSearch searchEJB;
	
	public DisplayMovieEntity getMovieById(Integer id) throws Exception{
		return displayMovieEJB.getDisplayMovie(id);
	}
	
	public List<MovieEntity> getAllMovies(){
		List<MovieEntity> allMovies = new ArrayList<MovieEntity>();
		allMovies = searchEJB.searchOrderByTitleDesc("", 0);
		System.out.println("Get all movies");
		for (int i = 0; i < allMovies.size(); i++) {
			System.out.println(allMovies.get(i));
		}
		return allMovies;
	}
	
	public List<MovieEntity> getMovieByTitleAscending(String title){
		return searchEJB.searchOrderByTitleAsc(title, 0);
	}
	
	public List<MovieEntity> getMovieByTitleDescending(String title){
		return searchEJB.searchOrderByTitleDesc(title, 0);
	}
	
	public List<MovieEntity> getMovieByTitleDateAscending(String title){
		return searchEJB.searchOrderByDateAsc(title, 0);
	}
	
	public List<MovieEntity> getMovieByTitleDateDescending(String title){
		return searchEJB.searchOrderByDateDesc(title, 0);
	}
	
}
