package utils;

import app.Movie;
import java.util.Comparator;

/**
 * Comparator pro porovnavani filmu na zaklade hodnoceni
 * @author Jaroslav Vít
 */
public class CompareByRating implements Comparator<Movie> {

    @Override
    public int compare(Movie m1, Movie m2) {
        return (int) (m2.getHodnoceni() * 10 - m1.getHodnoceni() * 10);
    }

}
