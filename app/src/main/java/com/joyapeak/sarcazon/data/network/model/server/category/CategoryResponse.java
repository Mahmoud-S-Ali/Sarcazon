package com.joyapeak.sarcazon.data.network.model.server.category;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 12/2/2018.
 */

public class CategoryResponse {

    public static class CategoriesResponse {

        @SerializedName("categories")
        private List<SingleCategory> categories;

        public CategoriesResponse(List<SingleCategory> categories) {
            this.categories = categories;
        }

        public List<SingleCategory> getCategories() {
            return categories;
        }
        public void setCategories(List<SingleCategory> categories) {
            this.categories = categories;
        }

        @Override
        public String toString() {
            return "CategoriesResponse{" +
                    "categories=" + categories +
                    '}';
        }
    }

    public static class SingleCategory {

        @SerializedName("name")
        private String name;

        @SerializedName("photo_url")
        private String photoUrl;

        public SingleCategory(String name, String photoUrl) {
            this.photoUrl = photoUrl;
            this.name = name;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }
        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "SingleCategory{" +
                    "photoUrl='" + photoUrl + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
