package search4.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import search4.ejb.DisplayMovieEJB;
import search4.ejb.interfaces.LocalDisplayMovie;
import search4.ejb.interfaces.LocalSearch;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;

@Path("/movies")
public class MovieResource {

	@EJB
	private LocalDisplayMovie displayMovieEJB;
	@EJB
	private LocalSearch searchEJB;
	
	@GET
	@Path("/{movieId}")
	@Produces(MediaType.APPLICATION_JSON)
	public DisplayMovieEntity getMovieById(@PathParam("movieId") Integer movieId) throws Exception {
		return displayMovieEJB.getDisplayMovie(movieId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieEntity> getAllMovies() throws Exception {
		List<MovieEntity> allMovies = searchEJB.searchOrderByTitleAsc("", 0);
		return allMovies;
	}
	
	@GET
	@Path("/title/asc/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieEntity> getMoviesOrderByTitleAsc(@PathParam("value") String value) {
		List<MovieEntity> result = searchEJB.searchOrderByTitleAsc(value, 0);
		return result;
	}
	
	@GET
	@Path("/title/desc/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieEntity> getMoviesOrderByTitleDesc(@PathParam("value") String value) {
		List<MovieEntity> result = searchEJB.searchOrderByTitleDesc(value, 0);
		return result;
	}
	
	@GET
	@Path("/date/asc/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieEntity> getMoviesOrderByDateAsc(@PathParam("value") String value) {
		List<MovieEntity> result = searchEJB.searchOrderByDateAsc(value, 0);
		return result;
	}
	
	@GET
	@Path("/date/desc/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieEntity> getMoviesOrderByDateDesc(@PathParam("value") String value) {
		List<MovieEntity> result = searchEJB.searchOrderByDateDesc(value, 0);
		return result;
	}
	
	
	
}
