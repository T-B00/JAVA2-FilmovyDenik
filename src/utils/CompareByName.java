package utils;

import app.Movie;
import java.util.Comparator;

/**
 * Comparator pro porovnavani na zaklade jmena filmu
 * @author Jaroslav Vít
 */
public class CompareByName implements Comparator<Movie> {

    @Override
    public int compare(Movie m1, Movie m2) {
        return m1.getJmeno().compareTo(m2.getJmeno());
    }

}
