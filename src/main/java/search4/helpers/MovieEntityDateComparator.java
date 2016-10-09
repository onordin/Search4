package search4.helpers;

import java.util.Comparator;

import search4.entities.MovieEntity;

public class MovieEntityDateComparator implements Comparator<MovieEntity>{

	public int compare(MovieEntity o1, MovieEntity o2) {
		String date1 = o1.getDate().toString();
	    String date2= o2.getDate().toString();

	      //ascending order
	      return date1.compareTo(date2);		
	}

	

}
