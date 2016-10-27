package search4.resources;

import static javax.ws.rs.core.Response.Status.OK;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import search4.ejb.interfaces.LocalProvider;
import search4.entities.DisplayProviderEntity;
import search4.entities.ProviderEntity;

@Stateless
@Path("/providers")
public class ProviderResource {

	/*get by userid
	get by providerID
	getAll providers
	add provider
	delete provider*/
	
	@EJB
	private LocalProvider providerEJB;
	
	@GET
	@Path("/userid/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DisplayProviderEntity> getProvidersByUserId(@PathParam("userId") Integer userId){
		return providerEJB.getAllForUser(userId);
	}
	
	@GET
	@Path("/providerId/{providerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProviderByProviderId(@PathParam("providerId") Integer providerId){
		DisplayProviderEntity displayProviderEntity = providerEJB.getProviderById(providerId);
		return Response.status(OK)
				.entity(displayProviderEntity)
				.build();
	}
	
	public Response getAllProviders(){
		return null;
	}
	
	@POST
	@Path("/addprovider/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProvider(String provider,@PathParam("userId") Integer userId){
		providerEJB.addProvider(provider, userId);
		return Response.status(OK)
				.build();
	}
	
	public Response updateProvider(){
		return null;
	}
	
	@DELETE
	@Path("/removeprovider/{providerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeProvider(@PathParam("providerId")Integer providerId){
		providerEJB.removeProvider(providerId);
		return Response.status(OK)
				.build();
	}
}
