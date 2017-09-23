package com.example.codepath.nytimesapp.rest;

import java.util.Map;

import com.example.codepath.nytimesapp.models.SearchApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by gretel on 9/19/17.
 */

public interface Service {
    @GET("articlesearch.json")
    Call<SearchApiResponse> getSearchResults(@QueryMap Map<String, String> options);

}
