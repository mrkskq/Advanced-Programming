package labs.lab3;

import java.io.*;
import java.util.*;

class Movie{
    private String title;
    private String genre;
    private int year;
    private double avgRating;

    public Movie(String title, String genre, int year, double avgRating) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.avgRating = avgRating;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getAvgRating() {
        return avgRating;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %.2f",title,genre,year,avgRating);
    }

    public static final Comparator<Movie> byGenre = Comparator.comparing(Movie::getGenre);
    public static final Comparator<Movie> byTitle = Comparator.comparing(Movie::getTitle);
    public static final Comparator<Movie> byYear = Comparator.comparing(Movie::getYear);
    public static final Comparator<Movie> byRating = Comparator.comparing(Movie::getAvgRating);
}

class MovieTheater{
    private ArrayList<Movie> movies;

    public MovieTheater() {
        movies = new ArrayList<Movie>();
    }

    public void readMovies(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int n = Integer.parseInt(br.readLine());
        for (int i = 0; i < n; i++) {
            String title = br.readLine();
            String genre = br.readLine();
            int year = Integer.parseInt(br.readLine());
            String []ratings = br.readLine().split("\\s+");
            double rating;
            double sum = 0;
            for (int j = 0; j < ratings.length; j++) {
                sum += Double.parseDouble(ratings[j]);
            }
            rating = sum / ratings.length;
            movies.add(new Movie(title, genre, year, rating));
        }
    }

    public void printByGenreAndTitle(){
        movies.sort(Movie.byGenre.thenComparing(Movie.byTitle));
        movies.forEach(System.out::println);
    }

    public void printByYearAndTitle(){
        movies.sort(Movie.byYear.thenComparing(Movie.byTitle));
        movies.forEach(System.out::println);
    }

    public void printByRatingAndTitle(){
        movies.sort(Movie.byRating.reversed().thenComparing(Movie.byTitle));
        movies.forEach(System.out::println);
    }
}

public class MovieTheaterTester {
    public static void main(String[] args) {
        MovieTheater mt = new MovieTheater();
        try {
            mt.readMovies(System.in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("SORTING BY RATING");
        mt.printByRatingAndTitle();
        System.out.println("\nSORTING BY GENRE");
        mt.printByGenreAndTitle();
        System.out.println("\nSORTING BY YEAR");
        mt.printByYearAndTitle();
    }
}