package com.joyapeak.sarcazon.data.network.model.server.user.register;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud Ali on 4/19/2018.
 */

public class RegisterResponse {

    private RegisterResponse() {}

    public static class BasicRegisterResponse implements Parcelable {

        @SerializedName("user_id")
        private Long userId;

        @SerializedName("access_token")
        private String accessToken;

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getAccessToken() {
            return accessToken;
        }
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BasicRegisterResponse)) return false;

            BasicRegisterResponse that = (BasicRegisterResponse) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            return accessToken != null ? accessToken.equals(that.accessToken) : that.accessToken == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (userId != null ? userId.hashCode() : 0);
            result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "BasicRegisterRequest{" +
                    "userId=" + userId +
                    ", accessToken='" + accessToken + '\'' +
                    '}';
        }

        protected BasicRegisterResponse(Parcel in) {
            userId = in.readByte() == 0x00 ? null : in.readLong();
            accessToken = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (userId == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeLong(userId);
            }
            dest.writeString(accessToken);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<BasicRegisterResponse> CREATOR = new Parcelable.Creator<BasicRegisterResponse>() {
            @Override
            public BasicRegisterResponse createFromParcel(Parcel in) {
                return new BasicRegisterResponse(in);
            }

            @Override
            public BasicRegisterResponse[] newArray(int size) {
                return new BasicRegisterResponse[size];
            }
        };
    }
}
