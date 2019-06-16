package com.joyapeak.sarcazon.data.network.model.server.other;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public class OtherRequest {

    public static class ReportRequest {

        private Long contentId;
        private Integer reportType;

        public ReportRequest(Long contentId, Integer reportType) {
            this.contentId = contentId;
            this.reportType = reportType;
        }

        public Long getContentId() {
            return contentId;
        }
        public void setContentId(Long contentId) {
            this.contentId = contentId;
        }

        public Integer getReportType() {
            return reportType;
        }
        public void setReportType(Integer reportType) {
            this.reportType = reportType;
        }

        @Override
        public String toString() {
            return "ReportRequest{" +
                    ", contentId=" + contentId +
                    ", reportType=" + reportType +
                    '}';
        }
    }

    public static class FeaturedReleaseRequest {

        private String message;
        private String topic;

        public FeaturedReleaseRequest(String message, String topic) {
            this.message = message;
            this.topic = topic;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public String getTopic() {
            return topic;
        }
        public void setTopic(String topic) {
            this.topic = topic;
        }

        @Override
        public String toString() {
            return "FeaturedReleaseRequest{" +
                    "message='" + message + '\'' +
                    ", topic='" + topic + '\'' +
                    '}';
        }
    }

    public static class MarkReviewedRequest {

        private Long reportId;
        private Integer reviewAction;
        private Integer userAction;

        public MarkReviewedRequest(Long reportId, Integer reviewAction, Integer userAction) {
            this.reportId = reportId;
            this.reviewAction = reviewAction;
            this.userAction = userAction;
        }

        public Long getReportId() {
            return reportId;
        }
        public void setReportId(Long reportId) {
            this.reportId = reportId;
        }

        public Integer getReviewAction() {
            return reviewAction;
        }
        public void setReviewAction(Integer reviewAction) {
            this.reviewAction = reviewAction;
        }

        public Integer getUserAction() {
            return userAction;
        }
        public void setUserAction(Integer userAction) {
            this.userAction = userAction;
        }

        @Override
        public String toString() {
            return "MarkReviewedRequest{" +
                    "reportId=" + reportId +
                    ", reviewAction=" + reviewAction +
                    ", userAction=" + userAction +
                    '}';
        }
    }

    public static class NextFeaturedWaitingHoursRequest {

        private Integer hours;

        public NextFeaturedWaitingHoursRequest(Integer hours) {
            this.hours = hours;
        }

        public Integer getHours() {
            return hours;
        }
        public void setHours(Integer hours) {
            this.hours = hours;
        }

        @Override
        public String toString() {
            return "NextFeaturedWaitingTimeRequest{" +
                    "hours=" + hours +
                    '}';
        }
    }
}
