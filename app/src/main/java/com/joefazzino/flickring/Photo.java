package com.joefazzino.flickring;

import java.io.Serializable;

/**
 * Created by joefazzino on 10/08/2016.
 */

// Class represents all the elements within a single photo object such as name, author etc
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mTitle;
    private String mAuthor;
    private String mAuthorID;
    private String mLink;
    private String mTags;
    private String mImage;

    // When class object is created all the parameters must be entered
    public Photo(String mTitle, String mAuthor, String mAuthorID, String mLink, String mTags, String mImage) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mAuthorID = mAuthorID;
        this.mLink = mLink;
        this.mTags = mTags;
        this.mImage = mImage;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthorID() {
        return mAuthorID;
    }

    public String getLink() {
        return mLink;
    }

    public String getTags() {
        return mTags;
    }

    public String getImage() {
        return mImage;
    }

    // Helpful for checking correct output
    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorID='" + mAuthorID + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
