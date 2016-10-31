package search4.entities;

import java.util.List;

import search4.helpers.ResourceLink;


public class DisplayUserEntity{

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<ResourceLink> links;
    
    
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

	public List<ResourceLink> getLinks() {
		return links;
	}

	public void setLinks(List<ResourceLink> links) {
		this.links = links;
	}


    
    

}
