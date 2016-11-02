package search4.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the providers database table.
 * 
 */
@Entity
@Table(name="providers")
@NamedQueries({
	@NamedQuery(name="ProviderEntity.getAllFor", query="SELECT p FROM ProviderEntity p where p.userId = :userId"),
	@NamedQuery(name="ProviderEntity.getOne", query="SELECT p FROM ProviderEntity p WHERE p.id = :id"),
	@NamedQuery(name="ProviderEntity.search", query="SELECT p FROM ProviderEntity p WHERE p.provider LIKE :input OR p.provider LIKE :inputWithSpace"),
	@NamedQuery(name="ProviderEntity.deleteUser", query="DELETE FROM ProviderEntity p WHERE p.userId = :userId")
})
public class ProviderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String provider;

	@Column(name="user_id")
	private int userId;

	public ProviderEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}