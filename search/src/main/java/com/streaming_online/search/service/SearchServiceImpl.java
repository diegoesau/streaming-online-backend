package com.streaming_online.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streaming_online.search.model.Movie;
import com.streaming_online.search.model.MovieES;
import com.streaming_online.search.repository.es.MovieESRepository;
import com.streaming_online.search.repository.jpa.MovieRepository;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieESRepository movieESRepository;

    @Override
    public String syncAllToElasticsearch() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieES> moviesES = movies.stream()
            .map(this::convertToES)
            .collect(Collectors.toList());

        movieESRepository.saveAll(moviesES);
        return "Total: " + movies.size() + " movies";
    }

    @Override
    public List<MovieES> multiSearch(String query) {
        return movieESRepository.multiFieldSearch(query);
    }

    @Override
    public List<MovieES> searchByTitle(String title) {
        return movieESRepository.findByTitleContaining(title);
    }

    @Override
    public List<MovieES> searchByDirector(String director) {
        return movieESRepository.findByDirectorContaining(director);
    }

    @Override
    public List<MovieES> searchByYear(Integer year) {
        return movieESRepository.findByYear(year);
    }

    @Override
    public List<MovieES> searchByGenre(String genre) {
        return movieESRepository.findByGenreContaining(genre);
    }

    @Override
    public List<MovieES> searchByImdbID(String imdbID) {
        return movieESRepository.findByImdbID(imdbID);
    }

    @Override
    public List<MovieES> searchByPlot(String plot) {
        return movieESRepository.findByPlotContaining(plot);
    }

    private MovieES convertToES(Movie movie) {
        return MovieES.builder()
            .imdbID(movie.getImdbID())
            .title(movie.getTitle())
            .year(movie.getYear())
            .genre(movie.getGenre())
            .director(movie.getDirector())
            .plot(movie.getPlot())
            .build();
    }
}

