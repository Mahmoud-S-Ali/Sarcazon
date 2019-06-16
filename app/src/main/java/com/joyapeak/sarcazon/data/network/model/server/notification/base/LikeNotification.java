package com.joyapeak.sarcazon.data.network.model.server.notification.base;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class LikeNotification {

    private ComicDataNotification comicData;
    private UserDataNotification userData;
    private Integer count;
    private Long date;

    public LikeNotification(ComicDataNotification comicData, UserDataNotification userData,
                                 Integer count, Long date) {
        this.comicData = comicData;
        this.userData = userData;
        this.count = count;
        this.date = date;
    }

    public ComicDataNotification getComicData() {
        return comicData;
    }
    public void setComicData(ComicDataNotification comicData) {
        this.comicData = comicData;
    }

    public UserDataNotification getUserData() {
        return userData;
    }
    public void setUserData(UserDataNotification userData) {
        this.userData = userData;
    }

    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getDate() {
        return date;
    }
    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikeNotification)) return false;

        LikeNotification that = (LikeNotification) o;

        if (comicData != null ? !comicData.equals(that.comicData) : that.comicData != null)
            return false;
        if (userData != null ? !userData.equals(that.userData) : that.userData != null)
            return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = comicData != null ? comicData.hashCode() : 0;
        result = 31 * result + (userData != null ? userData.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "comicData=" + comicData +
                ", userData=" + userData +
                ", count=" + count +
                ", date=" + date;
    }
}
