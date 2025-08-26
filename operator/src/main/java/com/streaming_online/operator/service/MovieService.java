/* Movie service interface 
 * Defines methods for the app operations
*/

package com.streaming_online.operator.service;

import com.streaming_online.operator.model.Movie;
import com.streaming_online.operator.model.MovieCollection;
import com.streaming_online.operator.model.MovieList;
import com.streaming_online.operator.model.MovieList.MovieState;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    /* App Operations */

    List<Movie> getAllMovies();

    List<Movie> getMoviesByGenre(String genre);

    List<MovieCollection> getAllMoviesGroupedByGenre();

    Movie getMovie(String imdbID);

    /* User Operations */

    List<Movie> getMovieList(String userID, MovieState state);

    Optional<MovieList> updateMovieState(String userID, String imdbID, String State);

    MovieList saveMovieToMyList(MovieList movieList);

    Boolean deleteMovieFromMyList(String userID, String imdbID);

    String getMovieState(String userID, String imdbID);

    /* Admin Operations */

    Movie addMovie(Movie movie);

    Movie updateMovie(String imdbID, java.util.Map<String, Object> updates);

    Boolean deleteMovie(String imdbID);
}
