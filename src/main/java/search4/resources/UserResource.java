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

import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplayUserEntity;
import search4.exceptions.DataNotFoundException;
import search4.helpers.Link;



@Path("/users")
public class UserResource{

	
	@EJB
	private LocalUser userEJB;
	
	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@Context UriInfo uriInfo, @PathParam("userId") Integer userId) {
		
		DisplayUserEntity displayUserEntity;
		
		try{
			displayUserEntity = userEJB.getUserByID(userId);
			displayUserEntity.setPassword("");
			
			return Response
					.status(Response.Status.OK)
					.entity(displayUserEntity)
					.build();
		}catch(DataNotFoundException dE) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers(@Context UriInfo uriInfo) {
		
		List<DisplayUserEntity> allUsers;
		GenericEntity< List <DisplayUserEntity> > entity;
		
		try{
			allUsers = userEJB.getAllUsers();
			
			for(DisplayUserEntity displayUserEntity : allUsers) {
				List<Link> links = new ArrayList<Link>();
				Link link = new Link("self", uriInfo.getAbsolutePath() + "/" + displayUserEntity.getId());
				links.add(link);
				displayUserEntity.setLinks(links);
			}

			entity = new GenericEntity< List <DisplayUserEntity> > (allUsers){};
			
			return Response
					.status(Response.Status.OK)
					.entity(entity)
					.build();
		}catch(Exception ex) {
			System.out.println("Inne i catch");
			return Response.status(Response.Status.NOT_FOUND).build();
			//return null;
		}
	}
	
	
	/*
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public 
	*/
	
	
	public String getAbsolutePath(UriInfo uriInfo) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(UserResource.class) //ger http://localhost:8080/MyMess/api/messages
				.build()
				.toString();
		return uri;
	}
	
}
