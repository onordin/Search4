package search4.resources;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import search4.ejb.DisplayMovieEJB;
import search4.ejb.interfaces.LocalDisplayMovie;
import search4.entities.DisplayMovieEntity;

@Path("/movies")
public class MovieResource {

	@EJB
	private LocalDisplayMovie displayMovieEJB;
	
	
	@GET
	@Path("/{movieId}")
	@Produces(MediaType.APPLICATION_JSON)
	public DisplayMovieEntity getMovie(@PathParam("movieId") Integer movieId) throws Exception {
		System.out.println("inside Rest API");
		return displayMovieEJB.getDisplayMovie(movieId);
	}
	
}
