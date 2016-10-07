package search4.helpers;

import java.util.Comparator;

import search4.entities.MovieEntity;

public class MovieEntityTitleComparator implements Comparator<MovieEntity> {

	@Override
	public int compare(MovieEntity o1, MovieEntity o2) {
			
		String title1 = o1.getTitle().toUpperCase();
	    String title2 = o2.getTitle().toUpperCase();

	      //ascending order
	      return title1.compareTo(title2);		
	}

}
