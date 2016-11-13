package search4.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.BadRequestException;

import search4.daobeans.DisplayMovieDAOBean;
import search4.daobeans.SubscriptionDAOBean;
import search4.daobeans.UserDAOBean;
import search4.ejb.interfaces.LocalSubscription;
import search4.entities.*;
import search4.exceptions.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class SubscriptionEJB implements LocalSubscription{

	@EJB
	private SubscriptionDAOBean subscriptionDAOBean;
    @EJB
    private UserDAOBean userDAOBean;
    @EJB
    private DisplayMovieDAOBean movieDAOBean;
	@EJB
	private DisplayMovieDAOBean displayMovieDAOBean;

    public DisplaySubscriptionEntity getSubscription(Integer id) throws DataNotFoundException {
        SubscriptionEntity subscriptionEntity = subscriptionDAOBean.getSubscription(id);
        return dbEntityToDisplayEntity(subscriptionEntity);
    }
    public List<DisplaySubscriptionEntity> getAll() {
        List<SubscriptionEntity> subscriptionEntities = subscriptionDAOBean.getAll();
        List<DisplaySubscriptionEntity> displaySubscriptionEntities = new ArrayList<DisplaySubscriptionEntity>();
        for (SubscriptionEntity subscriptionEntity : subscriptionEntities) {
            displaySubscriptionEntities.add(dbEntityToDisplayEntity(subscriptionEntity));
        }
        return displaySubscriptionEntities;
    }

	public List<DisplaySubscriptionEntity> getAllFor(Integer userId) throws DataNotFoundException {
		List<SubscriptionEntity> list = subscriptionDAOBean.getAllFor(userId);
		List<DisplaySubscriptionEntity> displayEntities = new ArrayList<DisplaySubscriptionEntity>();
		for (SubscriptionEntity se : list) {
			displayEntities.add(dbEntityToDisplayEntity(se));
		}
		return displayEntities;
	}

	private DisplaySubscriptionEntity dbEntityToDisplayEntity(SubscriptionEntity se) {
		DisplaySubscriptionEntity displayEntity = new DisplaySubscriptionEntity();
		displayEntity.setSubscribedMovieId(se.getMovieId());
		displayEntity.setId(se.getId());
        displayEntity.setSubscribedUserId(se.getUserId());
		try {
			MovieEntity movieEntity = displayMovieDAOBean.getMovieData(se.getMovieId());
			displayEntity.setTitle(movieEntity.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return displayEntity;
	}

	public InfoPayload subscribeToMovie(Integer movieId, Integer userId) {
		InfoPayload infoPayload = new InfoPayload();
		SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
		subscriptionEntity.setMovieId(movieId);
		subscriptionEntity.setUserId(userId);

		if (!subscriptionDAOBean.subscribeToMovie(subscriptionEntity)) {
            if (!userDAOBean.userExist(userId)) {
                infoPayload.setUser_Message("Failed to subscribe: No such user!");
                infoPayload.setStatusCode(400);
            }
            else if (!displayMovieDAOBean.movieExists(movieId)) {
                infoPayload.setUser_Message("Failed to subscribe: No such movie!");
                infoPayload.setStatusCode(400);
            }
            else {
                infoPayload.setUser_Message("Failed to subscribe: Unknown Error");
                infoPayload.setStatusCode(500);
            }
            infoPayload.setResultOK(false);
        }
        else {
            infoPayload.setUser_Message("Subscription Successfull!");
            infoPayload.setStatusCode(200);
            infoPayload.setResultOK(true);
        }
        return infoPayload;
	}

	public InfoPayload removeSubscription(Integer id) {
        InfoPayload infoPayload = new InfoPayload();
        if (subscriptionDAOBean.removeSubscription(id)) {
            infoPayload.setUser_Message("Successfully removed subscription!");
            infoPayload.setStatusCode(200);
            infoPayload.setResultOK(true);
        }
        else {
            infoPayload.setUser_Message("Error removing subscription with id "+id);
            infoPayload.setStatusCode(400); 
            infoPayload.setResultOK(false);
        }
		return infoPayload;
	}

}
