package search4.resources;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import search4.ejb.interfaces.LocalProvider;
import search4.entities.DisplayProviderEntity;
import search4.entities.ProviderEntity;

@Stateless
@Path("/providers")
public class ProviderResource {
	
	@Context
	UriInfo uriInfo;
	
	@EJB
	private LocalProvider providerEJB;
	
	@GET
	@Path("/userid/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProvidersByUserId(@PathParam("userId") Integer userId){
		List<DisplayProviderEntity> providers = new ArrayList<DisplayProviderEntity>();
		GenericEntity< List<DisplayProviderEntity> > entity;
		providers = providerEJB.getAllForUser(userId);
		entity = new GenericEntity<List<DisplayProviderEntity>>(providers){};
		return Response.status(200)
				.entity(entity)
				.build();
	}
	
	@GET
	@Path("/providerid/{providerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProviderByProviderId(@PathParam("providerId") Integer providerId){
		DisplayProviderEntity displayProviderEntity = providerEJB.getProviderById(providerId);
		return Response.status(200)
				.entity(displayProviderEntity)
				.build();
	}
	
	/*
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<DisplayProviderEntity> getAllProviders(){
		List<DisplayProviderEntity> allProviders = providerEJB.getAllProviders("");
		GenericEntity<List<DisplayProviderEntity>>entity;
		entity = new GenericEntity<List<DisplayProviderEntity>>(allProviders){};
		return providerEJB.getAllProviders("");
	}*/
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProviders(){
		List<DisplayProviderEntity> allProviders = providerEJB.getAllProviders("");
		GenericEntity<List<DisplayProviderEntity>>entity;
		entity = new GenericEntity<List<DisplayProviderEntity>>(allProviders){};
		return Response.status(200)
				.entity(entity)
				.build();
	}
	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProvider(String provider,@PathParam("userId")Integer userId){
		ProviderEntity providerEntity = new ProviderEntity();
		providerEntity.setProvider(provider);
		providerEntity.setUserId(userId);
		providerEJB.addProvider(providerEntity.getProvider(),providerEntity.getUserId());
		return Response.status(200)
				.entity(providerEntity)
				.build();
	}
	
	@DELETE
	@Path("/{providerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeProvider(@PathParam("providerId")Integer providerId){
		providerEJB.removeProvider(providerId);
		return Response.status(200)
				.build();
	}
	
}
