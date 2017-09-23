package com.example.codepath.nytimesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by gretel on 9/19/17.
 */
@Parcel
public class Response {

    @SerializedName("docs")
    @Expose
    public List<Doc> docs = null;

    // empty constructor for Parcelable
    public Response() {}

    public List<Doc> getDocs() {
        return docs;
    }

    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }
}
