package com.example.epasyaaar;

import java.util.Date;

public class TouristSpotReview_Data {

    String userID;
    String email;
    String photo;
    String review;
    float rating;
    String documentID;

    Date timestamp;





    public TouristSpotReview_Data( float rating, String documentID, Date timestamp) {
        this.userID = userID;
        this.email = email;
        this.photo = photo;
        this.review = review;
        this.rating = rating;
        this.documentID = documentID;
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
