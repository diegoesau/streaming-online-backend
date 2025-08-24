package com.streaming_online.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streaming_online.search.service.SearchService;

@RestController
@RequestMapping("/sync")
public class SyncController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/all-to-es")
    public ResponseEntity<String> syncAllToElasticsearch() {
        String result = searchService.syncAllToElasticsearch();
        return ResponseEntity.ok(result);
    }
}