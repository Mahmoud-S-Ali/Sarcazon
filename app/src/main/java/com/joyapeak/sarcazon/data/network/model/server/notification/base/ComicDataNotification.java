package com.joyapeak.sarcazon.data.network.model.server.notification.base;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public class ComicDataNotification {

    private Long comicId;
    private String comicPhotoUrl;

    public ComicDataNotification(Long comicId, String comicPhotoUrl) {
        this.comicId = comicId;
        this.comicPhotoUrl = comicPhotoUrl;
    }

    public Long getComicId() {
        return comicId;
    }
    public void setComicId(Long comicId) {
        this.comicId = comicId;
    }

    public String getComicPhotoUrl() {
        return comicPhotoUrl;
    }
    public void setComicPhotoUrl(String comic_photo_url) {
        this.comicPhotoUrl = comic_photo_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComicDataNotification)) return false;

        ComicDataNotification that = (ComicDataNotification) o;

        if (comicId != null ? !comicId.equals(that.comicId) : that.comicId != null) return false;
        return comicPhotoUrl != null ? comicPhotoUrl.equals(that.comicPhotoUrl) : that.comicPhotoUrl == null;
    }

    @Override
    public int hashCode() {
        int result = comicId != null ? comicId.hashCode() : 0;
        result = 31 * result + (comicPhotoUrl != null ? comicPhotoUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "comicId=" + comicId +
                ", comic_photo_url='" + comicPhotoUrl;
    }
}
