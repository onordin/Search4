package search4.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the subscriptions database table.
 * 
 */
@Entity
@Table(name="subscriptions")
@NamedQuery(name="SubscriptionEntity.findAll", query="SELECT s FROM SubscriptionEntity s")
public class SubscriptionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="movie_id")
	private int movieId;

	@Column(name="user_id")
	private int userId;

	public SubscriptionEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMovieId() {
		return this.movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}