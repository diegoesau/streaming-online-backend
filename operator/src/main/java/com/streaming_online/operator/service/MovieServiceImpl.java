/* Movie service implementation
 */

package com.streaming_online.operator.service;

import com.streaming_online.operator.model.Movie;
import com.streaming_online.operator.model.MovieCollection;
import com.streaming_online.operator.model.MovieList;
import com.streaming_online.operator.model.MovieList.MovieState;
import com.streaming_online.operator.repository.MovieRepository;
import com.streaming_online.operator.repository.MovieListRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    public List<MovieCollection> getAllMoviesGroupedByGenre() {
        List<Movie> allMovies = movieRepository.findAll();
        Map<String, Set<Movie>> genreMap = new HashMap<>();
    
        for (Movie movie : allMovies) {
            if (movie.getGenre() != null) {
                String[] genres = movie.getGenre().split(",");
                for (String g : genres) {
                    String genre = g.trim();
                    genreMap
                        .computeIfAbsent(genre, k -> new LinkedHashSet<>())
                        .add(movie);
                }
            }
        }
    
        return genreMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 6) // at least 6 movies
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size())) // sort by popularity
                .map(entry -> {
                    List<Movie> movieList = new ArrayList<>(entry.getValue());
                    Collections.shuffle(movieList);
                    
                    return MovieCollection.builder()
                            .name(entry.getKey())
                            .movies(movieList.stream().limit(10).toList()) // max 10
                            .build();
                })
                .collect(Collectors.toList());
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
    @Transactional
    public Optional<MovieList> updateMovieState(String userID, String imdbID, String newState) {
        if (newState == null) throw new RuntimeException("state is required");
    
        if ("NONE".equalsIgnoreCase(newState)) {
            movieListRepository.findByUserIDAndImdbID(userID, imdbID)
                .ifPresent(movieListRepository::delete);
            return Optional.empty(); // NONE => frontend interpreta como null
        }
    
        // Validar que la movie existe en catálogo
        movieRepository.findByImdbID(imdbID)
            .orElseThrow(() -> new RuntimeException("Movie not found with imdbID: " + imdbID));
    
        // Convertir a MovieState
        MovieState target;
        try {
            target = MovieState.valueOf(newState.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid movie state: " + newState);
        }
    
        // Buscar o crear registro en la lista del usuario
        MovieList entity = movieListRepository.findByUserIDAndImdbID(userID, imdbID)
            .orElseGet(() -> MovieList.builder()
                .userID(userID)
                .imdbID(imdbID)
                .build());
    
        entity.setState(target);
        return Optional.of(movieListRepository.save(entity));
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

    @Override
    public String getMovieState(String userID, String imdbID) {
        MovieState state = movieListRepository.findByUserIDAndImdbID(userID, imdbID)
            .map(MovieList::getState)
            .orElse(null);
        return state != null ? state.toString() : "NONE";
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

