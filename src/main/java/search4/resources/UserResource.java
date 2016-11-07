package search4.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import search4.ejb.interfaces.LocalProvider;
import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplayUserEntity;
import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;
import search4.helpers.ResourceLink;
import search4.entities.InfoPayload;


@Stateless
@Path("/users")
public class UserResource implements Serializable{

	
	private static final long serialVersionUID = 5432349798883745096L;

	
	@EJB
	private LocalUser userEJB;
	
	@Inject
	private Instance<ProviderResource> providerResource;
	
	@Inject
	private Instance<SubscriptionResource> subscriptionResource;
	
	
	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@Context UriInfo uriInfo, @PathParam("userId") Integer userId) {
		
		DisplayUserEntity displayUserEntity;
		
		try{
			displayUserEntity = userEJB.getUserByID(userId);
			displayUserEntity.setPassword("");
			
			List<ResourceLink> links = new ArrayList<ResourceLink>();
			ResourceLink self = new ResourceLink("self", uriInfo.getBaseUri().toString() + "users/" + displayUserEntity.getId());
			links.add(self);
			displayUserEntity.setLinks(links);
			
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
				List<ResourceLink> links = new ArrayList<ResourceLink>();
				ResourceLink self = new ResourceLink("self", uriInfo.getBaseUri().toString() + "users/" + displayUserEntity.getId());
				links.add(self);
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
	
	
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(UserEntity userEntity, @Context UriInfo uriInfo) {
		
		
		DisplayUserEntity displayUserEntity;
		
		
		try{			
			displayUserEntity = userEJB.createUser(userEntity);
			
			displayUserEntity.setPassword("");
			
			List<ResourceLink> links = new ArrayList<ResourceLink>();
			ResourceLink self = new ResourceLink("self", uriInfo.getBaseUri().toString() + "users/" + displayUserEntity.getId());
			links.add(self);
			displayUserEntity.setLinks(links);
			
			return Response
					.status(Response.Status.OK)
					.entity(displayUserEntity)
					.build();
		}catch(Exception ex) {
			ex.printStackTrace();
			return Response
					.status(Response.Status.BAD_REQUEST)
					.build();
		}
	}
	
	

	
	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUserDetails(@PathParam("userId") Integer userId, DisplayUserEntity displayUserEntity, @Context UriInfo uriInfo) {
		
		InfoPayload infoPayload = new InfoPayload();
		
		try{
			infoPayload = userEJB.updateUserDetails(displayUserEntity);
			if(infoPayload.isResultOK()) {
				infoPayload.setMore_Info(uriInfo.getBaseUri().toString() + "users/" + infoPayload.getInternal_Message());		//id has been put in InternalMessage variable
				return Response
						.status(Response.Status.OK)
						.entity(infoPayload)
						.build();
			}else {
				return Response
						.status(Response.Status.BAD_REQUEST)
						.entity(infoPayload)
						.build();
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			infoPayload.setUser_Message(ex.getMessage());
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(infoPayload)
					.build();
		}
		
		
	}
	
	
	
	@DELETE
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("userId") Integer userId, @Context UriInfo uriInfo) {
		
		InfoPayload resultFromDelete = new InfoPayload();
		
			resultFromDelete = userEJB.deleteUser(userId);
			System.out.println(resultFromDelete.getUser_Message());
			if(resultFromDelete.isResultOK()){
				return Response
						.status(Response.Status.OK)
						.entity(resultFromDelete)
						.build();
			}else {
				return Response
						.status(Response.Status.NOT_FOUND)
						.entity(resultFromDelete)
						.build();
			}
	}
	
	

	@GET
	@Path("/{userId}/providers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProvidersForUser(@PathParam("userId") Integer userId) {
		return providerResource.get().getProvidersByUserId(userId);
	}
	
	
	
	@GET
	@Path("/{userId}/subscriptions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubscriptionsForUser(@PathParam("userId") Integer userId) {
		return subscriptionResource.get().getAllMoviesForUser(userId);
	}
	
	
	
	
	
	
	
	
}
