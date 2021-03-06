package search4.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="users", schema = "search4")
@NamedQueries({
		@NamedQuery(name="UserEntity.getAll", query="SELECT u from UserEntity u"),
		@NamedQuery(name="UserEntity.getUserByEmail", query="SELECT userEntity FROM UserEntity userEntity WHERE userEntity.email = :email"),
		@NamedQuery(name="UserEntity.getUserById", query="SELECT userEntity FROM UserEntity userEntity WHERE userEntity.id = :id"),
		@NamedQuery(name="UserEntity.deleteUserById", query="DELETE FROM UserEntity userEntity WHERE userEntity.id = :id")
})
public class UserEntity implements Serializable {
	
	private static final long serialVersionUID = -798718815627738402L;

	@Id
	private int id;

	private String email;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	private String password;

	public UserEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}