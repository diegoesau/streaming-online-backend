package com.streaming_online.search.service;

import java.util.List;
import com.streaming_online.search.model.MovieES;

public interface SearchService {
    String syncAllToElasticsearch();

    List<MovieES> multiSearch(String query);

    List<MovieES> searchByTitle(String title);

    List<MovieES> searchByDirector(String director);

    List<MovieES> searchByYear(Integer year);

    List<MovieES> searchByGenre(String genre);

    List<MovieES> searchByImdbID(String imdbID);

    List<MovieES> searchByPlot(String plot);
}
