package search4.resources;

import search4.ejb.interfaces.LocalSubscription;
import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.SubscriptionEntity;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

//TODO add links
@Path("/subscriptions")
public class SubscriptionResource {

    @EJB
    private LocalSubscription subscriptionEJB;
    @EJB
    private LocalUser userEJB;

    //TODO GET on subscription id?

    @GET
	@Path("/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("userId") Integer userId) throws Exception {
        try {
            List<DisplaySubscriptionEntity> result = subscriptionEJB.getAllFor(userId);
            return Response
                    .ok()
                    .entity(result) //TODO add links here
                    .build();
        } catch (BadRequestException bre) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
	}

    @GET
    @Path("/movie/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DisplayUserEntity> getMovie(@PathParam("movieId") Integer movieId) throws Exception {
        return userEJB.getDisplayUsersSubscribedTo(movieId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubscriptionEntity> getSubscriptions() throws Exception {
        return subscriptionEJB.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSubscription(SubscriptionEntity subscriptionEntity) {
        try {
            subscriptionEJB.subscribeToMovie(subscriptionEntity.getMovieId(), subscriptionEntity.getUserId());
            return Response.ok("All is fine").build(); //TODO better response
        } catch (BadRequestException bre) {
            return Response.status(Response.Status.BAD_REQUEST).build(); //TODO better structure all the way down
        }
    }

    @DELETE
    @Path("/{subscriptionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSubscription(@PathParam("subscriptionId") Integer id) {
        try {
            subscriptionEJB.removeSubscription(id);
            return Response.ok().build();
        } catch (BadRequestException bre) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
