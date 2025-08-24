package com.streaming_online.search.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.streaming_online.search.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    List<Movie> findAll();
    
    Optional<Movie> findByImdbID(String imdbID);
    
}