package models;

import android.os.Parcelable;
import android.text.TextUtils;

import com.example.codepath.nytimesapp.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

import static android.R.attr.thumbnail;

/**
 * Created by gretel on 9/19/17.
 */
@Parcel
public class Doc implements Serializable {

    @SerializedName("web_url")
    @Expose
    public String webUrl;
    @SerializedName("snippet")
    @Expose
    public String snippet;
    @SerializedName("lead_paragraph")
    @Expose
    public String leadParagraph;
    @SerializedName("multimedia")
    @Expose
    public List<Multimedia> multimedia = null;
    @SerializedName("headline")
    @Expose
    public Headline headline;
    @SerializedName("pub_date")
    @Expose
    public String pubDate;
    @SerializedName("document_type")
    @Expose
    public String documentType;
    @SerializedName("news_desk")
    @Expose
    public String newsDesk;
    @SerializedName("section_name")
    @Expose
    public String sectionName;
    @SerializedName("subsection_name")
    @Expose
    public String subsectionName;
    @SerializedName("_id")
    @Expose
    public String id;

    public int colorId;

    // empty constructor for Parcelable


    public Doc() {}

   /* //Constructor for database
    public Doc(String id, String webUrl, Headline headline) {
        this.id = id;
        this.webUrl = webUrl;
        this.headline = headline;
    }*/

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getLeadParagraph() {
        return leadParagraph;
    }

    public void setLeadParagraph(String leadParagraph) {
        this.leadParagraph = leadParagraph;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public void setNewsDesk(String newsDesk) {
        this.newsDesk = newsDesk;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSubsectionName() {
        return subsectionName;
    }

    public void setSubsectionName(String subsectionName) {
        this.subsectionName = subsectionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean hasImage() {
        return this.getImageAddress() != null;
    }

    public String getImageAddress() {
        for (String subtype : new String[]{ "xlarge", "wide", "thumbnail"}) {
            for (Multimedia multimedia : this.getMultimedia()) {
                if ("image".equals(multimedia.getType()) && subtype.equals(multimedia.getSubtype())) {
                    return multimedia.getFullUrl();
                }
            }
        }
        for (Multimedia m : this.getMultimedia()) {
            if ("image".equals(m.getType())) {
                return m.getFullUrl();
            }
        }
        return null;
    }

    private void setNewsDesk(JSONObject jsonObject) throws JSONException {
        newsDesk = jsonObject.getString("news_desk");
        if (newsDesk.equalsIgnoreCase("null") || newsDesk.equalsIgnoreCase("none")) {
            // clean up some data
            newsDesk = "";
        }
    }

    private void setColorId() {
        colorId = R.color.accent;
            if (!TextUtils.isEmpty(newsDesk)) {
                if (newsDesk.equalsIgnoreCase("arts")) {
                    colorId = R.color.news_desk_art;
                } else if (newsDesk.equalsIgnoreCase("sports")) {
                    colorId = R.color.news_desk_sports;
                } else if (newsDesk.equalsIgnoreCase("fashion & style")) {
                    colorId = R.color.news_desk_fashion;
                }
            }
        }


}



