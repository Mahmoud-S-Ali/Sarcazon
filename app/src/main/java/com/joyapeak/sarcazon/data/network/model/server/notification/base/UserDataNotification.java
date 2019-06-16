package com.joyapeak.sarcazon.data.network.model.server.notification.base;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class UserDataNotification {

    private Long userId;
    private String name;
    private String photoUrl;

    public UserDataNotification(Long userId, String name, String photoUrl) {
        this.userId = userId;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDataNotification)) return false;
        if (!super.equals(o)) return false;

        UserDataNotification that = (UserDataNotification) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return photoUrl != null ? photoUrl.equals(that.photoUrl) : that.photoUrl == null;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
