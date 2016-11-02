package search4.ejb.interfaces;

import java.util.List;
import javax.ejb.Local;

import search4.entities.DisplayMovieEntity;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.InfoPayload;
import search4.entities.SubscriptionEntity;

@Local
public interface LocalSubscription {

	List<SubscriptionEntity> getAll();

	InfoPayload subscribeToMovie(Integer movieId, Integer userId);
	
	List<DisplaySubscriptionEntity> getAllFor(Integer userId) throws Exception;

	InfoPayload removeSubscription(Integer id);
}
