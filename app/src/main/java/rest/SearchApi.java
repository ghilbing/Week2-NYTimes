package rest;

import android.content.Context;
import android.util.Log;

import com.example.codepath.nytimesapp.R;

import java.io.IOException;
import java.util.Map;

import models.Filters;
import models.SearchApiResponse;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gretel on 9/19/17.
 */

public class SearchApi {



    private static final String API_KEY = "585a4d9704ea4858a0c05676d3a9a22b";
    private static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private static final Retrofit retrofit = retrofit();

    private static Retrofit retrofit() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("api-key", API_KEY)
                        .build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void search(Filters filters, int page, Callback<SearchApiResponse> cb) {
        Service service = retrofit.create(Service.class);
        Map<String, String> query = filters.queryMap();
        Log.d("DEBUG", "Search page " + page);
        query.put("page", ""  + page);
        Call<SearchApiResponse> call = service.getSearchResults(query);
        call.enqueue(cb);
    }

}
