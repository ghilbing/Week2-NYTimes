package rest;

import android.util.Log;

import java.util.Map;

import models.SearchApiResponse;
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
