package search4.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplayUserEntity;
import search4.exceptions.DataNotFoundException;

@Path("/users")
public class UserResource {

	@EJB
	private LocalUser userEJB;
	
	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@PathParam("userId") Integer userId) {
		DisplayUserEntity displayUserEntity = new DisplayUserEntity();
		try{
			displayUserEntity = userEJB.getUserByID(userId);
			displayUserEntity.setPassword("");
			return Response.status(Response.Status.OK).entity(displayUserEntity).build();
		}catch(DataNotFoundException dE) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<DisplayUserEntity> getAllUsers() {
		List<DisplayUserEntity> allUsers;
		try{
			System.out.println("inne i getAllUsers");
			return userEJB.getAllUsers();
			//return Response.status(Response.Status.OK).entity(allUsers).build();
		}catch(Exception ex) {
			System.out.println("Inne i catch");
			//return Response.status(Response.Status.NOT_FOUND).build();
			return null;
		}
	}
	
}
