package net.mankindenemy.diploma.projektProgramowanieAndroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

public class User implements Parcelable {

    private String userName, userNumber, userEmail, userBio, key, userImage, userPassword, userId, userSchools, userLocation, userStatus;
    private Object time;


    public User(String userName, String userImage, String userId) {

        this.userName = userName;
        this.userImage = userImage;
        this.userId = userId;
        this.time = ServerValue.TIMESTAMP;
    }

    public User(String userName, String userNumber, String userEmail, String userBio, String key, String userImage, String userPassword, String userId, String userSchools, String userLocation, String userStatus) {

        this.userName = userName;
        this.userNumber = userNumber;
        this.userEmail = userEmail;
        this.userBio = userBio;
        this.key = key;
        this.userImage = userImage;
        this.userPassword = userPassword;
        this.userId = userId;
        this.userSchools = userSchools;
        this.userLocation = userLocation;
        this.userStatus = userStatus;
    }

    public User() {

    }

    public String getUserStatus() {

        return userStatus;
    }

    public void setUserStatus(String userStatus) {

        this.userStatus = userStatus;
    }

    public String getUserSchools() {

        return userSchools;
    }

    public void setUserSchools(String userSchools) {

        this.userSchools = userSchools;
    }

    public String getUserLocation() {

        return userLocation;
    }

    public void setUserLocation(String userLocation) {

        this.userLocation = userLocation;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getUserNumber() {

        return userNumber;
    }

    public void setUserNumber(String userNumber) {

        this.userNumber = userNumber;
    }

    public String getUserEmail() {

        return userEmail;
    }

    public void setUserEmail(String userEmail) {

        this.userEmail = userEmail;
    }

    public String getUserBio() {

        return userBio;
    }

    public void setUserBio(String userBio) {

        this.userBio = userBio;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getUserImage() {

        return userImage;
    }

    public void setUserImage(String userImage) {

        this.userImage = userImage;
    }

    public String getUserPassword() {

        return userPassword;
    }

    public void setUserPassword(String userPassword) {

        this.userPassword = userPassword;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public Object getTime() {

        return time;
    }

    public void setTime(Object time) {

        this.time = time;
    }

    protected User(Parcel in) {

        userName = in.readString();
        userNumber = in.readString();
        userEmail = in.readString();
        userBio = in.readString();
        key = in.readString();
        userImage = in.readString();
        userPassword = in.readString();
        userId = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {

        @Override
        public User createFromParcel(Parcel in) {

            return new User(in);
        }

        @Override
        public User[] newArray(int size) {

            return new User[size];
        }
    };

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(userName);
        dest.writeString(userNumber);
        dest.writeString(userEmail);
        dest.writeString(userBio);
        dest.writeString(key);
        dest.writeString(userImage);
        dest.writeString(userPassword);
        dest.writeString(userId);
    }

}
