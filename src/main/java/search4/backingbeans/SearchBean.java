package search4.backingbeans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.swing.event.ListSelectionEvent;

import search4.ejb.interfaces.LocalSearch;
import search4.entities.MovieEntity;
import search4.helpers.MovieEntityDateComparator;
import search4.helpers.MovieEntityTitleComparator;

@Named(value="searchBean") //if we want to use another name to call this from the JSF file
@SessionScoped
public class SearchBean implements Serializable {

	private static final long serialVersionUID = 8335492042247599634L;
	
	private String query;
    private List<MovieEntity> movieEntities;
    private boolean titleSort;
    private boolean ascending;
    private String sortByParameter;
    private String sortHow;
    private Integer limit;

    @EJB
    private LocalSearch searchEJB;
    
    public SearchBean() {
		sortByParameter = "title";
		sortHow = "ascending";
	}
    
    public void cleanSearch() {
    	query = "";
    	movieEntities.clear();
    }

    public String search(Integer setLimit) {
    	limit = setLimit;
    	movieEntities = searchEJB.searchOrderByTitleAsc(query, limit);
    	updateListSortByParameter();
    	
    	/*
        titleSort = true;
        ascending = false;
        if (titleSort && ascending) {
            movieEntities = searchEJB.searchOrderByTitleAsc(query, limit);
        }
        else if (titleSort && !ascending) {
            movieEntities = searchEJB.searchOrderByTitleDesc(query, limit);
        }
        else if (!titleSort && ascending) {
            movieEntities = searchEJB.searchOrderByDateAsc(query, limit);
        }
        else {
            movieEntities = searchEJB.searchOrderByDateDesc(query, limit);
        }
        */
        return "full_startpage";
    }

    
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<MovieEntity> getMovieEntities() {
        return movieEntities;
    }

    public void setMovieEntities(List<MovieEntity> movieEntities) {
        this.movieEntities = movieEntities;
    }
    
    
    public boolean isTitleSort() {
		return titleSort;
	}

	public void setTitleSort(boolean titleSort) {
		this.titleSort = titleSort;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	
	public String getSortByParameter() {
		return sortByParameter;
	}



	public void setSortByParameter(String sortByParameter) {
		this.sortByParameter = sortByParameter;
	}



	public String getSortHow() {
		return sortHow;
	}



	public void setSortHow(String sortHow) {
		this.sortHow = sortHow;
	}



	public void updateListSortByParameter() {
		if(sortByParameter.equals("title") && !movieEntities.isEmpty()) {
			Collections.sort(movieEntities, new MovieEntityTitleComparator());
			if(sortHow.equals("descending")){
				Collections.reverse(movieEntities);
			}
		}else if(!movieEntities.isEmpty()) {
			Collections.sort(movieEntities, new MovieEntityDateComparator());	
			if(sortHow.equals("descending")){
				Collections.reverse(movieEntities);
			}
		}
    }
	

	public void updateListSortByHow() {
		
	}
	
	
	public String ajaxListener(AjaxBehaviorEvent event) {
    	return search(10);
    }
	
	public String ajaxListenerLargeSearch(AjaxBehaviorEvent event) {
    	return search(0);
    }
    
}
