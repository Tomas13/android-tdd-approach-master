package com.example.androidtdd.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by quanlt on 19/01/2017.
 */

public class Photo extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private int userId;
    private int albumId;
    private String title;
    private String url;
    private String thumbnailUrl;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.albumId);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.thumbnailUrl);
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.userId = in.readInt();
        this.albumId = in.readInt();
        this.id = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        if (id != photo.id) return false;
        if (userId != photo.userId) return false;
        if (albumId != photo.albumId) return false;
        if (title != null ? !title.equals(photo.title) : photo.title != null) return false;
        if (url != null ? !url.equals(photo.url) : photo.url != null) return false;
        return thumbnailUrl != null ? thumbnailUrl.equals(photo.thumbnailUrl) : photo.thumbnailUrl == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + albumId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
        return result;
    }
}
