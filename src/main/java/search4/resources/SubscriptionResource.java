package search4.resources;

import search4.ejb.interfaces.LocalSubscription;
import search4.ejb.interfaces.LocalUser;
import search4.entities.*;
import search4.helpers.ResourceLink;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("/subscriptions")
public class SubscriptionResource {

    @EJB
    private LocalSubscription subscriptionEJB;
    @EJB
    private LocalUser userEJB;
    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{subscriptionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubscription(@PathParam("subscriptionId") Integer subscriptionId) {
        try {
            DisplaySubscriptionEntity subscriptionEntity = subscriptionEJB.getSubscription(subscriptionId);
            List<ResourceLink> links = new ArrayList<ResourceLink>();
            ResourceLink movieLink = new ResourceLink("movie", uriInfo.getBaseUri() + "movies/" + subscriptionEntity.getSubscribedMovieId());
            ResourceLink userLink = new ResourceLink("user", uriInfo.getBaseUri() + "users/" + subscriptionEntity.getSubscribedUserId());
            links.add(movieLink);
            links.add(userLink);
            subscriptionEntity.setLinks(links);

            GenericEntity<DisplaySubscriptionEntity> entity = new GenericEntity<DisplaySubscriptionEntity>(subscriptionEntity){};
            return Response
                    .status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            return Response
                    .status(400)
                    .build();
        }
    }

    @GET
	@Path("/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMoviesForUser(@PathParam("userId") Integer userId) {
        try {
            List<DisplaySubscriptionEntity> subscriptionsForUser = subscriptionEJB.getAllFor(userId);
            for (DisplaySubscriptionEntity displaySubscriptionEntity : subscriptionsForUser) {
                List<ResourceLink> links = new ArrayList<ResourceLink>();
                ResourceLink link = new ResourceLink("movie", uriInfo.getBaseUri() + "movies/" + displaySubscriptionEntity.getSubscribedMovieId());
                links.add(link);
                displaySubscriptionEntity.setLinks(links);
            }
            GenericEntity<List<DisplaySubscriptionEntity>> entity = new GenericEntity<List<DisplaySubscriptionEntity>>(subscriptionsForUser){};
            return Response
                    .status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            return Response
                    .status(400)
                    .build();
        }
    }

    @GET
    @Path("/movie/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsersForMovie(@PathParam("movieId") Integer movieId) {
        try {
            List<DisplayUserEntity> usersSubscribed = userEJB.getDisplayUsersSubscribedTo(movieId);
            for (DisplayUserEntity displayUserEntity : usersSubscribed) {
                List<ResourceLink> links = new ArrayList<ResourceLink>();
                ResourceLink link = new ResourceLink("user", uriInfo.getBaseUri() + "users/" + displayUserEntity.getId());
                links.add(link);
                displayUserEntity.setLinks(links);
            }
            GenericEntity<List<DisplayUserEntity>> entity = new GenericEntity<List<DisplayUserEntity>>(usersSubscribed) {};
            return Response
                    .status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            return Response
                    .status(400)
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DisplaySubscriptionEntity> getSubscriptions() throws Exception {
        return subscriptionEJB.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSubscription(SubscriptionEntity subscriptionEntity) {
        InfoPayload infoPayload = subscriptionEJB.subscribeToMovie(subscriptionEntity.getMovieId(), subscriptionEntity.getUserId());
        return responseFactory(infoPayload);
    }

    @DELETE
    @Path("/{subscriptionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSubscription(@PathParam("subscriptionId") Integer id) {
        InfoPayload infoPayload = subscriptionEJB.removeSubscription(id);
        return responseFactory(infoPayload);
    }

    private Response responseFactory(InfoPayload infoPayload) {
        Integer status = infoPayload.getStatusCode();
        return Response
                .status(status)
                .entity(infoPayload)
                .build();
    }
}
