package search4.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import search4.daobeans.SearchDatabaseDAOBean;

@Stateless
public class SearchDatabaseEJB {

	@EJB
	private SearchDatabaseDAOBean searchDatabaseDAOBean;

	
	public String searchDatabaseFindTMDbId() {
		String TMDbId = searchDatabaseDAOBean.findTMDbIdFromTitle();
		return null;
	}
	
	
	
}
