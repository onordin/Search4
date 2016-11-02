package search4.resources.entities;

import java.util.ArrayList;
import java.util.List;
import search4.entities.DisplayUserEntity;

public class UserResourceEntity {

	private List<DisplayUserEntity> displayUserEntities;
	
	
	public UserResourceEntity() {
		displayUserEntities = new ArrayList<DisplayUserEntity>();
	}

	
	public List<DisplayUserEntity> getDisplayUserEntities() {
		return displayUserEntities;
	}

	public void setDisplayUserEntities(List<DisplayUserEntity> displayUserEntities) {
		this.displayUserEntities = displayUserEntities;
	}
	
	
	
}
