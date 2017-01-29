package com.dencaval.project01;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by denis on 08/10/2016.
 */
public class MovieParcelable implements Parcelable {
    private String originalTitle;
    private String posterPath;
    private String overview;
    private String userRating;
    private String releaseDate;

    public MovieParcelable(String originalTitle,
             String posterPath,
             String overview,
             String userRating,
             String releaseDate){
        this.setOriginalTitle(originalTitle);
        this.setPosterPath(posterPath);
        this.setOverview(overview);
        this.setUserRating(userRating);
        this.setReleaseDate(releaseDate);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getOriginalTitle());
        out.writeString(getPosterPath());
        out.writeString(getOverview());
        out.writeString(getUserRating());
        out.writeString(getReleaseDate());
    }

    public static final Parcelable.Creator<MovieParcelable> CREATOR
            = new Parcelable.Creator<MovieParcelable>() {
        public MovieParcelable createFromParcel(Parcel in) {
            return new MovieParcelable(in);
        }

        public MovieParcelable[] newArray(int size) {
            return new MovieParcelable[size];
        }
    };

    private MovieParcelable(Parcel in) {
        this.setOriginalTitle(in.readString());
        this.setPosterPath(in.readString());
        this.setOverview(in.readString());
        this.setUserRating(in.readString());
        this.setReleaseDate(in.readString());
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}