package com.joyapeak.sarcazon.data.network.model.server.notification.comic;

import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse;
import com.joyapeak.sarcazon.data.network.model.server.notification.base.ComicDataNotification;

/**
 * Created by Mahmoud Ali on 6/19/2018.
 */

public class ComicFeaturedNotification {

    private ComicDataNotification comicData;
    private Long date;

    public ComicFeaturedNotification(ComicDataNotification comicData, Long date) {
        this.comicData = comicData;
        this.date = date;
    }

    public ComicFeaturedNotification(NotificationResponse.ServerNotification serverNotification) {
        this (
                new ComicDataNotification(serverNotification.getComicId(), serverNotification.getComicPhotoUrl()),
                serverNotification.getDate());
    }

    public ComicDataNotification getComicData() {
        return comicData;
    }
    public void setComicData(ComicDataNotification comicData) {
        this.comicData = comicData;
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
        if (!(o instanceof ComicFeaturedNotification)) return false;

        ComicFeaturedNotification that = (ComicFeaturedNotification) o;

        if (comicData != null ? !comicData.equals(that.comicData) : that.comicData != null)
            return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = comicData != null ? comicData.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComicFeaturedNotification{" +
                "comicData=" + comicData +
                ", date=" + date +
                '}';
    }
}
