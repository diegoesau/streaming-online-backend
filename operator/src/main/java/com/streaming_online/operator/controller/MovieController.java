/* Movie controller class
 * Handles HTTP requests related to movies
 * It communicates with the service layer to process movie-related operations (endpoints)
 */
package com.streaming_online.operator.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;

import com.streaming_online.operator.service.MovieService;
import com.streaming_online.operator.model.Movie;
import com.streaming_online.operator.model.MovieCollection;
import com.streaming_online.operator.model.MovieList;
import com.streaming_online.operator.model.MovieList.MovieState;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController // Bean REST Controller type
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /* App Operations */

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();

        if (movies != null && !movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/movies/genre/{genre}")
    public ResponseEntity<MovieCollection> getMoviesByGenre(@PathVariable String genre) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);

        if (movies != null && !movies.isEmpty()) {
            MovieCollection movieCollection = MovieCollection.builder()
                    .name(genre)
                    .movies(movies)
                    .build();
            return ResponseEntity.ok(movieCollection);
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/movies/genres")
    public ResponseEntity<List<MovieCollection>> getAllMoviesByGenres() {
        List<MovieCollection> collections = movieService.getAllMoviesGroupedByGenre();
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/movies/{imdbID}")
    public ResponseEntity<Movie> getMovie(@PathVariable String imdbID) {
        Movie movie = movieService.getMovie(imdbID);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /* User Operations */

    @GetMapping("/movies/mylist/{userID}")
    public ResponseEntity<List<MovieCollection>> getAllMovieLists(@PathVariable String userID) {
        List<MovieCollection> collections = Arrays.stream(MovieState.values())
                .map(state -> {
                    List<Movie> movies = movieService.getMovieList(userID, state);
                    return MovieCollection.builder()
                            .name(state.toString())
                            .movies(movies)
                            .build();
                })
                .collect(Collectors.toList());
    
        return ResponseEntity.ok(collections);
    }

    @PatchMapping("/movies/mylist/{userID}/{imdbID}/{state}")
    public ResponseEntity<?> updateMovieState(
            @PathVariable String userID,
            @PathVariable String imdbID,
            @PathVariable String state) {
    
        Optional<MovieList> result = movieService.updateMovieState(userID, imdbID, state);
    
        return result
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }   

    @PutMapping("/movies/mylist")
    public ResponseEntity<MovieList> saveMovieToMyList(@RequestBody MovieList movieList) {
        MovieList savedMovie = movieService.saveMovieToMyList(movieList);
        return ResponseEntity.ok(savedMovie);
    }

    @DeleteMapping("/movies/mylist/{userID}/{imdbID}")
    public ResponseEntity<Boolean> deleteMovieFromMyList(@PathVariable String userID, @PathVariable String imdbID) {
        Boolean deleted = movieService.deleteMovieFromMyList(userID, imdbID);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/movies/state/{userId}/{imdbId}")
    public ResponseEntity<Map<String, String>> getMovieState(
            @PathVariable String userId,
            @PathVariable String imdbId) {
    
        String state = movieService.getMovieState(userId, imdbId);
        Map<String, String> res = new HashMap<>();
        res.put("state", state != null ? state : "NONE");
    
        return ResponseEntity.ok(res);
    }

    /* Admin Operations */

    @PutMapping("/movies")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie addedMovie = movieService.addMovie(movie);
        return ResponseEntity.ok(addedMovie);
    }
    
    @PatchMapping("/movies/{imdbID}")
    public ResponseEntity<Movie> updateMovie(@PathVariable String imdbID, @RequestBody Map<String, Object> updates) {
        Movie updatedMovie = movieService.updateMovie(imdbID, updates);
        return ResponseEntity.ok(updatedMovie);
    }
    
    @DeleteMapping("/movies/{imdbID}")
    public ResponseEntity<Boolean> deleteMovie(@PathVariable String imdbID) {
        Boolean deleted = movieService.deleteMovie(imdbID);
        return ResponseEntity.ok(deleted);
    }
}
