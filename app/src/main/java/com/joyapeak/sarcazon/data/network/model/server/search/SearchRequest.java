package com.joyapeak.sarcazon.data.network.model.server.search;

/**
 * Created by Mahmoud Ali on 6/13/2018.
 */

public class SearchRequest {

    public static class SearchComicsRequest extends SearchBaseRequest {

        public SearchComicsRequest(String query, Integer startPos, Integer offset) {
            super(query, startPos, offset);
        }
    }

    public static class SearchUsersRequest extends SearchBaseRequest {

        public SearchUsersRequest(String query, Integer startPos, Integer offset) {
            super(query, startPos, offset);
        }
    }


    private static class SearchBaseRequest {

        private String query;
        private Integer startPos;
        private Integer offset;

        public SearchBaseRequest(String query, Integer startPos, Integer offset) {
            this.query = query;
            this.startPos = startPos;
            this.offset = offset;
        }

        public String getQuery() {
            return query;
        }
        public void setQuery(String query) {
            this.query = query;
        }

        public Integer getStartPos() {
            return startPos;
        }
        public void setStartPos(Integer startPos) {
            this.startPos = startPos;
        }

        public Integer getOffset() {
            return offset;
        }
        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        @Override
        public String toString() {
            return "SearchBaseRequest{" +
                    "query='" + query + '\'' +
                    ", startPos=" + startPos +
                    ", offset=" + offset +
                    '}';
        }
    }
}
