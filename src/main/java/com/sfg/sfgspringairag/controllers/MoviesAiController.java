package com.sfg.sfgspringairag.controllers;

import com.sfg.sfgspringairag.model.MoviesRequest;
import com.sfg.sfgspringairag.model.MoviesResponse;
import com.sfg.sfgspringairag.services.OpenAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movies-recommendation")
public class MoviesAiController {

    private final OpenAiService openAiService;

    public MoviesAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping(path = "/movie-info")
    MoviesResponse getMoviesRecommendation(@RequestBody MoviesRequest moviesRequest) {
        return openAiService.getMoviesRecommendation(moviesRequest);
    }
}
