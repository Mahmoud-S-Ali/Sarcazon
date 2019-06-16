package com.joyapeak.sarcazon.data.network.model.server.subs;

import com.joyapeak.sarcazon.data.network.model.server.BaseItemsRequest;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public class SubsRequest {

    public static class SubRequest {

        private Long userId;
        private Long subId;
        private Integer value;

        public SubRequest(long userId, Long subId, Integer value) {
            this.userId = userId;
            this.subId = subId;
            this.value = value;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getSubId() {
            return subId;
        }
        public void setSubId(Long subId) {
            this.subId = subId;
        }

        public Integer getValue() {
            return value;
        }
        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SubRequest)) return false;

            SubRequest that = (SubRequest) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            if (subId != null ? !subId.equals(that.subId) : that.subId != null) return false;
            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (subId != null ? subId.hashCode() : 0);
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "SubscribeRequest{" +
                    "userId=" + userId +
                    ", subId=" + subId +
                    ", value=" + value +
                    '}';
        }
    }

    public static class UserSubsRequest extends BaseItemsRequest {

        private Long userId;
        private Long viewerId;

        public UserSubsRequest(Long userId, Long viewerId, Long baseId, Integer offset, Integer count) {
            super(baseId, offset, count);
            this.userId = userId;
            this.viewerId = viewerId;
        }

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getViewerId() {
            return viewerId;
        }
        public void setViewerId(Long viewerId) {
            this.viewerId = viewerId;
        }

        @Override
        public String toString() {
            return "UserSubsRequest{" +
                    "userId=" + userId +
                    ", viewerId=" + viewerId +
                    super.toString() +
                    '}';
        }
    }
}
