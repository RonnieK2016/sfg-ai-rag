package com.sfg.sfgspringairag.services;

import com.sfg.sfgspringairag.model.MoviesRequest;
import com.sfg.sfgspringairag.model.MoviesResponse;
import org.springframework.stereotype.Component;

public interface OpenAiService {

    MoviesResponse getMoviesRecommendation(MoviesRequest moviesRequest);
}
