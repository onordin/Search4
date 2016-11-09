package search4.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import search4.ejb.DisplayMovieEJB;
import search4.ejb.interfaces.LocalDisplayMovie;
import search4.ejb.interfaces.LocalSearch;
import search4.entities.DisplayMovieEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.MovieEntity;
import search4.helpers.ResourceLink;
import search4.resources.entities.MovieResourceEntity;

@Path("/movies")
public class MovieResource {

	@EJB
	private LocalDisplayMovie displayMovieEJB;
	@EJB
	private LocalSearch searchEJB;
	
	@GET
	@Path("/{movieId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMovieById(@Context UriInfo uriInfo, @PathParam("movieId") Integer movieId) {
		try {
			DisplayMovieEntity displayMovieEntity = displayMovieEJB.getDisplayMovie(movieId);
			
			List<ResourceLink> links = new ArrayList<ResourceLink>();
			ResourceLink self = new ResourceLink("self", uriInfo.getAbsolutePath().toString());
			links.add(self);
			displayMovieEntity.setLinks(links);
			
			return Response.status(Response.Status.OK).entity(displayMovieEntity).build();
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/getallmovies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMovies(@Context UriInfo uriInfo) {
		List<MovieEntity> allMovies;
		GenericEntity< List <MovieResourceEntity> > entity;
		List<MovieResourceEntity> movieEntityWrapped = new ArrayList<MovieResourceEntity>();
		try{
			allMovies = searchEJB.searchOrderByTitleAsc("", 0);
			for(MovieEntity movieEntity : allMovies) {
				List<ResourceLink> links = new ArrayList<ResourceLink>();
				String linkToDisplayMovieEntity = uriInfo.getAbsolutePath().toString();
				linkToDisplayMovieEntity = linkToDisplayMovieEntity.substring(0, linkToDisplayMovieEntity.length()-12);
				ResourceLink self = new ResourceLink("self", linkToDisplayMovieEntity + movieEntity.getId());
				MovieResourceEntity movieResourceEntity = new MovieResourceEntity();
				movieResourceEntity.setMovieEntity(movieEntity);
				movieResourceEntity.setResoureLink(self);
				movieEntityWrapped.add(movieResourceEntity);
			}
			entity = new GenericEntity< List <MovieResourceEntity> > (movieEntityWrapped){};
			return Response
					.status(Response.Status.OK)
					.entity(entity)
					.build();
		}catch(Exception ex) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/title/asc/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMoviesOrderByTitleAsc(@Context UriInfo uriInfo, @PathParam("value") String value) {
		List<MovieEntity> result;
		GenericEntity< List <MovieResourceEntity> > entity;
		List<MovieResourceEntity> movieEntityWrapped = new ArrayList<MovieResourceEntity>();
		try{
			result = searchEJB.searchOrderByTitleAsc(value, 0);
			for(MovieEntity movieEntity : result) {
				String uri = uriInfo.getAbsolutePath().toString();
				String linkToDisplayMovieEntity = uri.substring(0, uri.indexOf("title/asc"));
				ResourceLink self = new ResourceLink("self", linkToDisplayMovieEntity + movieEntity.getId());
				MovieResourceEntity movieResourceEntity = new MovieResourceEntity();
				movieResourceEntity.setMovieEntity(movieEntity);
				movieResourceEntity.setResoureLink(self);
				movieEntityWrapped.add(movieResourceEntity);
			}
			entity = new GenericEntity< List <MovieResourceEntity> > (movieEntityWrapped){};
			return Response
					.status(Response.Status.OK)
					.entity(entity)
					.build();
		}catch(Exception ex) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/title/desc/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMoviesOrderByTitleDesc(@Context UriInfo uriInfo, @PathParam("value") String value) {
		
		List<MovieEntity> result;
		GenericEntity< List <MovieResourceEntity> > entity;
		List<MovieResourceEntity> movieEntityWrapped = new ArrayList<MovieResourceEntity>();
		try{
			result = searchEJB.searchOrderByTitleDesc(value, 0);
			for(MovieEntity movieEntity : result) {
				String uri = uriInfo.getAbsolutePath().toString();
				String linkToDisplayMovieEntity = uri.substring(0, uri.indexOf("title/desc"));
				ResourceLink self = new ResourceLink("self", linkToDisplayMovieEntity + movieEntity.getId());
				MovieResourceEntity movieResourceEntity = new MovieResourceEntity();
				movieResourceEntity.setMovieEntity(movieEntity);
				movieResourceEntity.setResoureLink(self);
				movieEntityWrapped.add(movieResourceEntity);
			}
			entity = new GenericEntity< List <MovieResourceEntity> > (movieEntityWrapped){};
			return Response
					.status(Response.Status.OK)
					.entity(entity)
					.build();
		}catch(Exception ex) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/date/asc/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMoviesOrderByDateAsc(@Context UriInfo uriInfo, @PathParam("value") String value) {
		List<MovieEntity> result;
		GenericEntity< List <MovieResourceEntity> > entity;
		List<MovieResourceEntity> movieEntityWrapped = new ArrayList<MovieResourceEntity>();
		try{
			result = searchEJB.searchOrderByDateAsc(value, 0);
			for(MovieEntity movieEntity : result) {
				String uri = uriInfo.getAbsolutePath().toString();
				String linkToDisplayMovieEntity = uri.substring(0, uri.indexOf("date/asc"));
				ResourceLink self = new ResourceLink("self", linkToDisplayMovieEntity + movieEntity.getId());
				MovieResourceEntity movieResourceEntity = new MovieResourceEntity();
				movieResourceEntity.setMovieEntity(movieEntity);
				movieResourceEntity.setResoureLink(self);
				movieEntityWrapped.add(movieResourceEntity);
			}
			entity = new GenericEntity< List <MovieResourceEntity> > (movieEntityWrapped){};
			return Response
					.status(Response.Status.OK)
					.entity(entity)
					.build();
		}catch(Exception ex) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/date/desc/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMoviesOrderByDateDesc(@Context UriInfo uriInfo, @PathParam("value") String value) {
		List<MovieEntity> result;
		GenericEntity< List <MovieResourceEntity> > entity;
		List<MovieResourceEntity> movieEntityWrapped = new ArrayList<MovieResourceEntity>();
		try{
			result = searchEJB.searchOrderByDateDesc(value, 0);
			for(MovieEntity movieEntity : result) {
				String uri = uriInfo.getAbsolutePath().toString();
				String linkToDisplayMovieEntity = uri.substring(0, uri.indexOf("date/desc"));
				ResourceLink self = new ResourceLink("self", linkToDisplayMovieEntity + movieEntity.getId());
				MovieResourceEntity movieResourceEntity = new MovieResourceEntity();
				movieResourceEntity.setMovieEntity(movieEntity);
				movieResourceEntity.setResoureLink(self);
				movieEntityWrapped.add(movieResourceEntity);
			}
			entity = new GenericEntity< List <MovieResourceEntity> > (movieEntityWrapped){};
			return Response
					.status(Response.Status.OK)
					.entity(entity)
					.build();
		}catch(Exception ex) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	
	
}
