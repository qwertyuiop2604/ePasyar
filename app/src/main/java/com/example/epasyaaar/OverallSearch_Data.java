package com.example.epasyaaar;

public class OverallSearch_Data {

    private String name;
    private String photo;
    private String category;
    private String documentId2;

    public OverallSearch_Data() {
    }

    public OverallSearch_Data(String name, String photo, String category, String documentId2) {
        this.name = name;
        this.photo = photo;
        this.category = category;
        this.documentId2 = documentId2;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDocumentId2() {
        return documentId2;
    }

    public void setDocumentId2(String documentId2) {
        this.documentId2 = documentId2;
    }
}

