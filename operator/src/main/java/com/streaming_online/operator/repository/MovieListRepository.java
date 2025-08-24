package com.streaming_online.operator.repository;

import com.streaming_online.operator.model.MovieList;
import com.streaming_online.operator.model.MovieList.MovieState;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Long> {

    List<MovieList> findByUserID(String userID);
    Optional<MovieList> findByUserIDAndImdbID(String userID, String imdbID);
    List<MovieList> findByUserIDAndState(String userID, MovieState state);
    boolean existsByUserIDAndImdbID(String userID, String imdbID);
    
}
