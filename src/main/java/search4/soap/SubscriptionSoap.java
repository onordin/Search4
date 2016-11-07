package search4.soap;

import search4.ejb.interfaces.LocalSubscription;
import search4.ejb.interfaces.LocalUser;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.InfoPayload;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName="subscriptionSoap")
public class SubscriptionSoap {

    @EJB
    private LocalSubscription subscriptionEJB;
    @EJB
    private LocalUser userEJB;

    public DisplaySubscriptionEntity getSubscription(Integer subscriptionId) throws Exception{
        return subscriptionEJB.getSubscription(subscriptionId);
    }

    public List<DisplaySubscriptionEntity> getAllSubscriptionsForUser(Integer userId) throws Exception {
        return subscriptionEJB.getAllFor(userId);
    }

    public List<DisplayUserEntity> getAllUsersForMovie(Integer movieId) throws Exception {
        return userEJB.getDisplayUsersSubscribedTo(movieId);
    }

    public List<DisplaySubscriptionEntity> getAll() throws Exception {
        return subscriptionEJB.getAll();
    }

    public String addSubscription(Integer movieId, Integer userId) {
        InfoPayload infoPayload = subscriptionEJB.subscribeToMovie(movieId, userId);
        if (infoPayload.isResultOK()) {
            return "Subscription Added";
        }
        return "Failed to add subscription";
    }

    public String removeSubscription(Integer id) {
        InfoPayload infoPayload = subscriptionEJB.removeSubscription(id);
        if (infoPayload.isResultOK()) {
            return "Subscription Removed";
        }
        return "Failed to remove subscription";
    }
}
