package com.streaming_online.search.repository.es;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.streaming_online.search.model.MovieES;

@Repository
public interface MovieESRepository extends ElasticsearchRepository<MovieES, String> {
    
    List<MovieES> findByTitleContaining(String title);
    
    List<MovieES> findByDirectorContaining(String director);
    
    List<MovieES> findByYear(Integer year);
    
    List<MovieES> findByGenreContaining(String genre);

    List<MovieES> findByImdbID(String imdbID);

    List<MovieES> findByPlotContaining(String plot);

    /* Future Implementations */

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"director\", \"plot\", \"genre\", \"actors\"]}}")
    List<MovieES> multiFieldSearch(String query);
    
    @Query("{\"range\": {\"year\": {\"gte\": ?0, \"lte\": ?1}}}")
    List<MovieES> findByYearRange(Integer startYear, Integer endYear);
    
    @Query("{\"fuzzy\": {\"title\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<MovieES> fuzzySearchByTitle(String title);
}