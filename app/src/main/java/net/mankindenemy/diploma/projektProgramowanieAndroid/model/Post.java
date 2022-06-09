package net.mankindenemy.diploma.projektProgramowanieAndroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

public class Post implements Parcelable {

    private String post, postImage, publisherId, key;
    private Object time;

    public Post(String post, String postImage, String publisherId) {

        this.post = post;
        this.postImage = postImage;
        this.publisherId = publisherId;
        this.time = ServerValue.TIMESTAMP;
    }

    public Post() {

    }

    public String getPost() {

        return post;
    }

    public void setPost(String post) {

        this.post = post;
    }

    public String getPostImage() {

        return postImage;
    }

    public void setPostImage(String postImage) {

        this.postImage = postImage;
    }

    public String getPublisherId() {

        return publisherId;
    }

    public void setPublisherId(String publisherId) {

        this.publisherId = publisherId;
    }

    public Object getTime() {

        return time;
    }

    public void setTime(Object time) {

        this.time = time;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.post);
        dest.writeString(this.postImage);
        dest.writeString(this.publisherId);
        dest.writeString(this.key);
    }

    protected Post(Parcel in) {

        this.post = in.readString();
        this.postImage = in.readString();
        this.publisherId = in.readString();
        this.key = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {

        @Override
        public Post createFromParcel(Parcel source) {

            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {

            return new Post[size];
        }
    };

}
