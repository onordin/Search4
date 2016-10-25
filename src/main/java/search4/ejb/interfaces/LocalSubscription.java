package search4.ejb.interfaces;

import java.util.List;
import javax.ejb.Local;

import search4.entities.DisplayMovieEntity;
import search4.entities.DisplaySubscriptionEntity;

@Local
public interface LocalSubscription {

	public void subscribeToMovie(Integer movieId, Integer userId);
	
	public List<DisplaySubscriptionEntity> getAllFor(Integer userId);

	public boolean removeSubscription(Integer id);

	
}
