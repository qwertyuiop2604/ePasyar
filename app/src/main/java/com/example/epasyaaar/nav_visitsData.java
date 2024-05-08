package com.example.epasyaaar;

public class nav_visitsData {

    private String documentId;
    private String name;
    private String photo;
    private long timestamp;
    private String category;
    private boolean reviewSubmitted;

    public nav_visitsData(String documentId, String name, String photo, long timestamp, String category) {
        this.documentId = documentId;
        this.name = name;
        this.photo = photo;
        this.timestamp = timestamp;
        this.category = category;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isReviewSubmitted() {
        return reviewSubmitted;
    }

    public void setReviewSubmitted(boolean reviewSubmitted) {
        this.reviewSubmitted = reviewSubmitted;
    }
}
