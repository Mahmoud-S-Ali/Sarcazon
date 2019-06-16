package com.joyapeak.sarcazon.data.network.model.server;


/*
 * Used for any list of items
 * */

public class BaseItemsRequest {

    // baseId is the first item's returned id in the first request
    // This id is important so as not get duplicated posts with subsequent requests
    // in case new posts added since the first request
    Long baseId;
    Integer offset;
    Integer count;

    public BaseItemsRequest(Long baseId, Integer offset, Integer count) {
        this.baseId = baseId;
        this.offset = offset;
        this.count = count;
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseItemsRequest)) return false;

        BaseItemsRequest that = (BaseItemsRequest) o;

        if (baseId != null ? !baseId.equals(that.baseId) : that.baseId != null)
            return false;
        if (offset != null ? !offset.equals(that.offset) : that.offset != null) return false;
        return count != null ? count.equals(that.count) : that.count == null;
    }

    @Override
    public int hashCode() {
        int result = baseId != null ? baseId.hashCode() : 0;
        result = 31 * result + (offset != null ? offset.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ", baseId=" + baseId +
                ", offset=" + offset +
                ", count=" + count;
    }
}
