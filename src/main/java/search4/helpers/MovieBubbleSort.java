package search4.helpers;

import search4.entities.MovieEntity;

import java.util.List;

public class MovieBubbleSort {

    public List<MovieEntity> bubbleSort(List<MovieEntity> list) {
        System.out.println("tyring to sort list");
        boolean swapped = true;
        int j = 0;
        MovieEntity tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < list.size() - j; i++) {
                  if (list.get(i).getTmdbId() > list.get(i+1).getTmdbId()) {
                        tmp = list.get(i);
                        list.remove(i);
                        list.add(i+1, tmp);
                        swapped = true;
                  }
            }
        }
        System.out.println("LIST:: "+list);
        return list;
    }
}
