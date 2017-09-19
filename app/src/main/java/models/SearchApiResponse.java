package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gretel on 9/19/17.
 */

public class SearchApiResponse {

    @SerializedName("response")
    @Expose
    public Response response;

    // empty constructor for Parcelable
    public SearchApiResponse() {}


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
