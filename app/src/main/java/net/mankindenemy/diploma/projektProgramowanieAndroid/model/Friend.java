package net.mankindenemy.diploma.projektProgramowanieAndroid.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable {

    private String friendName, friendImage, friendId, friendRequestId, userStatus;
    private Object time;


    public Friend(String friendName, String friendImage, String friendId, String friendRequestId) {

        this.friendName = friendName;
        this.friendImage = friendImage;
        this.friendId = friendId;
        this.friendRequestId = friendRequestId;
    }


    public Friend() {

    }

    public String getUserStatus() {

        return userStatus;
    }

    public void setUserStatus(String userStatus) {

        this.userStatus = userStatus;
    }

    public String getFriendName() {

        return friendName;
    }

    public void setFriendName(String friendName) {

        this.friendName = friendName;
    }

    public String getFriendImage() {

        return friendImage;
    }

    public void setFriendImage(String friendImage) {

        this.friendImage = friendImage;
    }

    public String getFriendId() {

        return friendId;
    }

    public void setFriendId(String friendId) {

        this.friendId = friendId;
    }

    public String getFriendRequestId() {

        return friendRequestId;
    }

    public void setFriendRequestId(String friendRequestId) {

        this.friendRequestId = friendRequestId;
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

        dest.writeString(this.friendName);
        dest.writeString(this.friendImage);
        dest.writeString(this.friendId);
        dest.writeString(this.friendRequestId);
    }

    protected Friend(Parcel in) {

        this.friendName = in.readString();
        this.friendImage = in.readString();
        this.friendId = in.readString();
        this.friendRequestId = in.readString();
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {

        @Override
        public Friend createFromParcel(Parcel source) {

            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {

            return new Friend[size];
        }
    };

}
