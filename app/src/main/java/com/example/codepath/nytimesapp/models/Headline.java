package com.example.codepath.nytimesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by gretel on 9/19/17.
 */
@Parcel
public class Headline {

    @SerializedName("main")
    @Expose
    public String main;
    @SerializedName("print_headline")
    @Expose
    public String printHeadline;

    // empty constructor for Parcelable
    public Headline() {}

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getPrintHeadline() {
        return printHeadline;
    }

    public void setPrintHeadline(String printHeadline) {
        this.printHeadline = printHeadline;
    }
}
