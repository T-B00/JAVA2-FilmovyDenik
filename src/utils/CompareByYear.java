package utils;

import app.Movie;
import java.util.Comparator;

/**
 * Comparator pro porovnavani filmu na zaklade stari filmu
 * @author Jaroslav Vít
 */
public class CompareByYear implements Comparator<Movie> {

    @Override
    public int compare(Movie m1, Movie m2) {
        return m1.getRok() - m2.getRok();
    }

}
