/* Movie service implementation
 */

package com.streaming_online.operator.service;

import com.streaming_online.operator.model.Movie;
import com.streaming_online.operator.model.MovieList;
import com.streaming_online.operator.model.MovieList.MovieState;
import com.streaming_online.operator.repository.MovieRepository;
import com.streaming_online.operator.repository.MovieListRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieListRepository movieListRepository;

    /* App Operations */

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> movies = movieRepository.findByGenreContaining(genre);
        return movies.isEmpty() ? Collections.emptyList() : movies;
    }

    @Override
    public Movie getMovie(String imdbID) {
        return movieRepository.findByImdbID(imdbID).orElse(null);
    }

    /* User Operations */

    @Override
    public List<Movie> getMovieList(String userID, MovieState state) {
        List<MovieList> myList = movieListRepository.findByUserIDAndState(userID, state);
        List<String> imdbIDs = myList.stream()
                                    .map(MovieList::getImdbID)
                                    .collect(Collectors.toList());

        List<Movie> movies = movieRepository.findByImdbIDIn(imdbIDs);
        return movies.isEmpty() ? Collections.emptyList() : movies;
    }

    @Override
    public MovieList updateMovieState(String userID, String imdbID, String newState) {
        MovieList movieList = movieListRepository.findByUserIDAndImdbID(userID, imdbID)
            .orElseThrow(() -> new RuntimeException("Movie not found in list"));

        try {
            MovieState state = MovieState.valueOf(newState.toUpperCase());
            if (movieList.getState() == MovieState.SAVED && (state == MovieState.PURCHASED || state == MovieState.RENTED)) {
                movieList.setState(state);
            }
            else if (movieList.getState() == MovieState.RENTED && state == MovieState.PURCHASED) {
                movieList.setState(state);
            } else if (movieList.getState() == state) {
                // No state change
            } else {
                throw new RuntimeException("Invalid state transition from " + movieList.getState() + " to " + state);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid state: " + newState);
        }
        
        return movieListRepository.save(movieList);
    }

    @Override
    public MovieList saveMovieToMyList(MovieList movieList) {

        if (movieList.getState() == null) {
            throw new RuntimeException("State is required");
        }

        if (!movieRepository.findByImdbID(movieList.getImdbID()).isPresent()) {
            throw new RuntimeException("Movie not found with imdbID: " + movieList.getImdbID());
        }
        
        if (movieListRepository.existsByUserIDAndImdbID(movieList.getUserID(), movieList.getImdbID())) {
            throw new RuntimeException("Movie already in list");
        }
        
        return movieListRepository.save(movieList);
    }


    @Override
    public Boolean deleteMovieFromMyList(String userID, String imdbID) {
        MovieList movieList = movieListRepository.findByUserIDAndImdbID(userID, imdbID).orElse(null);
        if (movieList != null) {
            movieListRepository.delete(movieList);
            return true;
        }
        return false;
    }

    /* Admin Operations */

    @Override
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(String imdbID, Map<String, Object> updates) {
        Movie movie = movieRepository.findByImdbID(imdbID)
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        
        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    movie.setTitle((String) value);
                    break;
                case "genre":
                    movie.setGenre((String) value);
                    break;
                case "year":
                    movie.setYear((Integer) value);
                    break;
                case "director":
                    movie.setDirector((String) value);
                    break;
                case "plot":
                    movie.setPlot((String) value);
                    break;
                case "poster":
                    movie.setPoster((String) value);
                    break;
                case "runtime":
                    movie.setRuntime((String) value);
                    break;
                default:
                    throw new RuntimeException("Invalid field: " + key);
            }
        });
        
        return movieRepository.save(movie);
    }

    @Override
    public Boolean deleteMovie(String imdbID) {
        return movieRepository.findByImdbID(imdbID)
            .map(movie -> {
                movieRepository.delete(movie);
                return true;
            })
            .orElse(false);
    }
}

