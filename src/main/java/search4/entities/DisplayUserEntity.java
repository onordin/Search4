package search4.entities;

import search4.helpers.ResourceLink;


public class DisplayUserEntity{

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private ResourceLink link; 
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public ResourceLink getLink() {
		return link;
	}

	public void setLink(ResourceLink link) {
		this.link = link;
	}

    

}
