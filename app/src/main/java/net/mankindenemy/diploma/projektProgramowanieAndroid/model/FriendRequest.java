package net.mankindenemy.diploma.projektProgramowanieAndroid.model;

public class FriendRequest {

    private String requestId, requestTo, requestName, requestImage, key, keyPrevious, userStatus;

    public FriendRequest() {

    }

    public FriendRequest(String requestId, String requestTo, String requestName, String requestImage, String key, String keyPrevious, String userStatus) {

        this.requestId = requestId;
        this.requestTo = requestTo;
        this.requestName = requestName;
        this.requestImage = requestImage;
        this.key = key;
        this.keyPrevious = keyPrevious;
        this.userStatus = userStatus;
    }

    public String getUserStatus() {

        return userStatus;
    }

    public void setUserStatus(String userStatus) {

        this.userStatus = userStatus;
    }

    public String getKeyPrevious() {

        return keyPrevious;
    }

    public void setKeyPrevious(String keyPrevious) {

        this.keyPrevious = keyPrevious;
    }

    public String getRequestId() {

        return requestId;
    }

    public void setRequestId(String requestId) {

        this.requestId = requestId;
    }

    public String getRequestTo() {

        return requestTo;
    }

    public void setRequestTo(String requestTo) {

        this.requestTo = requestTo;
    }

    public String getRequestName() {

        return requestName;
    }

    public void setRequestName(String requestName) {

        this.requestName = requestName;
    }

    public String getRequestImage() {

        return requestImage;
    }

    public void setRequestImage(String requestImage) {

        this.requestImage = requestImage;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

}
