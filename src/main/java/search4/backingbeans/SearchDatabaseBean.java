package search4.backingbeans;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import search4.ejb.SearchDatabaseEJB;

@Named(value="searchDatabaseBean") //if we want to use another name to call this from the JSF file
@SessionScoped
public class SearchDatabaseBean implements Serializable{

	private static final long serialVersionUID = 6877418466879846851L;

	@EJB
   	SearchDatabaseEJB searchDatabaseEJB;
	
	public String searchMovie() {
        String result = searchDatabaseEJB.searchDatabaseFindTMDbId();
        return result;
    }
	
	
}
