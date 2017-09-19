package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gretel on 9/19/17.
 */

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
