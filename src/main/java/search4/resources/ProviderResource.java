package search4.resources;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
import search4.entities.InfoPayload;
import search4.entities.ProviderEntity;
import search4.helpers.ResourceLink;

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
		try{
			for (DisplayProviderEntity displayProviderEntity : providers) {
				List<ResourceLink> links = new ArrayList<ResourceLink>();
				ResourceLink link = new ResourceLink("self", uriInfo.getBaseUri()+ "providers/providerid/" + displayProviderEntity.getId());
				links.add(link);
				displayProviderEntity.setLinks(links);
			}
		
			entity = new GenericEntity<List<DisplayProviderEntity>>(providers){};
			return Response.status(200)
					.entity(entity)
					.build();
		}catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.build();
		}
	}
	
	@GET
	@Path("/providerid/{providerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProviderByProviderId(@PathParam("providerId") Integer providerId){
		try{
			DisplayProviderEntity displayProviderEntity = providerEJB.getProviderById(providerId);
			List<ResourceLink> links = new ArrayList<ResourceLink>();
			ResourceLink link = new ResourceLink("self", uriInfo.getBaseUri() +"providers/providerid/"+displayProviderEntity.getId());
			links.add(link);
			displayProviderEntity.setLinks(links);
			return Response.status(200)
					.entity(displayProviderEntity)
					.build();
		}catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.build();
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProviders(){
		System.out.println("getAll");
		List<DisplayProviderEntity> allProviders = providerEJB.getAllProviders("");
		GenericEntity<List<DisplayProviderEntity>>entity;
		try{
			for (DisplayProviderEntity displayProviderEntity : allProviders) {
				List<ResourceLink> links = new ArrayList<ResourceLink>();
				ResourceLink link = new ResourceLink("self", uriInfo.getAbsolutePath()+ "/providerid/"+displayProviderEntity.getId());
				links.add(link);
				displayProviderEntity.setLinks(links);
			}
			entity = new GenericEntity<List<DisplayProviderEntity>>(allProviders){};
			return Response.status(200)
					.entity(entity)
					.build();
		}catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.build();
		}
	}
	
	

	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProvider(String provider,@PathParam("userId")Integer userId){
		GenericEntity<List<DisplayProviderEntity>>entities;
		try{
			ProviderEntity providerEntity = new ProviderEntity();
			providerEntity.setProvider(provider);
			providerEntity.setUserId(userId);
			ProviderEntity entity = providerEJB.addProvider(providerEntity.getProvider(),providerEntity.getUserId());
			List<DisplayProviderEntity> providers = providerEJB.getAllForUser(entity.getUserId());
			for (DisplayProviderEntity displayProviderEntity : providers) {
				List<ResourceLink> links = new ArrayList<ResourceLink>();
				ResourceLink link = new ResourceLink("self", uriInfo.getBaseUri()+ "providers/providerid/" + displayProviderEntity.getId());
				links.add(link);
				displayProviderEntity.setLinks(links);
			}
			entities = new GenericEntity<List<DisplayProviderEntity>>(providers){};
			return Response.status(200)
					.entity(entities)
					.build();
		}catch (Exception e) {
			return Response.status(Response.Status.NO_CONTENT)
					.build();
		}
	}
	
	@DELETE
	@Path("/{providerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeProvider(@PathParam("providerId")Integer providerId){
		InfoPayload infoPayload = providerEJB.removeProviderById(providerId);
		return Response.status(200)
				.entity(infoPayload)
				.build();
	}
}