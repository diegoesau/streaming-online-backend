package com.streaming_online.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streaming_online.search.model.MovieES;
import com.streaming_online.search.service.SearchService;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/multi")
    public ResponseEntity<List<MovieES>> multiSearch(@RequestParam String q) {
        return ResponseEntity.ok(searchService.multiSearch(q));
    }

    @GetMapping("/title")
    public ResponseEntity<List<MovieES>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(searchService.searchByTitle(title));
    }

    @GetMapping("/director")
    public ResponseEntity<List<MovieES>> searchByDirector(@RequestParam String director) {
        return ResponseEntity.ok(searchService.searchByDirector(director));
    }

    @GetMapping("/year")
    public ResponseEntity<List<MovieES>> searchByYear(@RequestParam Integer year) {
        return ResponseEntity.ok(searchService.searchByYear(year));
    }

    @GetMapping("/genre")
    public ResponseEntity<List<MovieES>> searchByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(searchService.searchByGenre(genre));
    }

    @GetMapping("/imdbid")
    public ResponseEntity<List<MovieES>> searchByImdbID(@RequestParam String imdbID) {
        return ResponseEntity.ok(searchService.searchByImdbID(imdbID));
    }

    @GetMapping("/plot")
    public ResponseEntity<List<MovieES>> searchByPlot(@RequestParam String plot) {
        return ResponseEntity.ok(searchService.searchByPlot(plot));
    }
}

