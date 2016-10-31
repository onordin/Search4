package search4.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import search4.helpers.ResourceLink;


@Stateless
@Path("/users")
public class UserResource implements Serializable{

	
	private static final long serialVersionUID = 5432349798883745096L;
	
	
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
				//List<ResourceLink> links = new ArrayList<ResourceLink>();
				ResourceLink link = new ResourceLink("self", uriInfo.getAbsolutePath() + "/" + displayUserEntity.getId());
				//links.add(link);
				displayUserEntity.setLink(link);
			}
			
			entity = new GenericEntity< List <DisplayUserEntity> > (allUsers){};
			
			return Response
					.status(Response.Status.OK)
					.entity(allUsers)
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
