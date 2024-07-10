package com.ma.isw.bookcafe.model.mo;

public class Review {
    private int reviewId;
    private int rating;
    private String testo;
    private String ISBNBook;
    private int userId;
    private boolean deleted;

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getISBNBook() {
        return ISBNBook;
    }

    public void setISBNBook(String ISBNBook) {
        this.ISBNBook = ISBNBook;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

