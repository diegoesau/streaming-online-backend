package com.streaming_online.operator.repository;

import com.streaming_online.operator.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitle(String title);
    @Query("SELECT m FROM Movie m WHERE m.genre LIKE %:genre%")
    List<Movie> findByGenreContaining(@Param("genre") String genre);
    Optional<Movie> findByImdbID(String imdbID);
    List<Movie> findByImdbIDIn(List<String> imdbIDs);
}