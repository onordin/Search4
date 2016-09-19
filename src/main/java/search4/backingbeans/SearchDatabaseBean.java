package search4.backingbeans;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import search4.ejb.SearchDatabaseEJB;

@Named(value="searchDatabaseBean") //if we want to use another name to call this from the JSF file
@ApplicationScoped 
public class SearchDatabaseBean implements Serializable{

	private static final long serialVersionUID = 6877418466879846851L;
	private String title;

	
	@EJB
   	SearchDatabaseEJB searchDatabaseEJB;
	
	
	public String searchMovie() {
        Integer result = searchDatabaseEJB.searchDatabaseFindTMDbId(title);
        System.out.println("TMDB ID = " + result);
        if(result != null) {
        	int guideBoxId = searchDatabaseEJB.requestGuideBoxIdFromMovieTitle(title);
        	searchDatabaseEJB.printSourcesFromGuideBoxId(guideBoxId);
        	return "updated";
        }else {
        	return "";
        }
    }


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
