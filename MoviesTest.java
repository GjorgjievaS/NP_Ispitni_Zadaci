//package Најдобри_филмови;

import java.util.*;
import java.util.stream.Collectors;

class Movie{
    private String title;
    private int[] ratings;
    private double ratingCoef;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
        this.ratingCoef = 0;
    }

    public double average(){
        // return Arrays.stream(ratings).sum()/ Arrays.stream(ratings).count();
        return Arrays.stream(ratings).average().getAsDouble();
    }

    public void ratingCoef(int max){
        ratingCoef = average() * ratings.length / max;
    }

    public String getTitle() { return title; }

    public int[] getRatings() { return ratings; }

    public double getRatingCoef() { return ratingCoef; }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title,average(),ratings.length);
    }
}

class MoviesList {
    List<Movie> movies;

    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings){
        movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating(){
        return movies.stream()
                .sorted(Comparator.comparing(Movie::average).reversed().thenComparing(Movie::getTitle))
                .collect(Collectors.toList())
                .subList(0,10);
    }

    public List<Movie> top10ByRatingCoef(){
        movies.stream().forEach(m->m.ratingCoef(movies.size()));
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getRatingCoef).reversed().thenComparing(Movie::getTitle))
                .collect(Collectors.toList())
                .subList(0,10);
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
