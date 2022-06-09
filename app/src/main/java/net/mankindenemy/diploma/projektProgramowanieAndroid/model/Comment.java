package net.mankindenemy.diploma.projektProgramowanieAndroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

public class Comment implements Parcelable {

    private String comment, publisherName, publisherImage, publisherId, key;
    private Object time;

    public Comment() {

    }

    public Comment(String comment, String publisherName, String publisherImage, String publisherId) {

        this.comment = comment;
        this.publisherName = publisherName;
        this.publisherImage = publisherImage;
        this.publisherId = publisherId;
        this.time = ServerValue.TIMESTAMP;
    }

    public String getPublisherId() {

        return publisherId;
    }

    public void setPublisherId(String publisherId) {

        this.publisherId = publisherId;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

    public String getPublisherName() {

        return publisherName;
    }

    public void setPublisherName(String publisherName) {

        this.publisherName = publisherName;
    }

    public String getPublisherImage() {

        return publisherImage;
    }

    public void setPublisherImage(String publisherImage) {

        this.publisherImage = publisherImage;
    }

    public Object getTime() {

        return time;
    }

    public void setTime(Object time) {

        this.time = time;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.comment);
        dest.writeString(this.publisherName);
        dest.writeString(this.publisherImage);
    }

    protected Comment(Parcel in) {

        this.comment = in.readString();
        this.publisherName = in.readString();
        this.publisherImage = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {

        @Override
        public Comment createFromParcel(Parcel source) {

            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {

            return new Comment[size];
        }
    };

}
