package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.Review;
import com.ma.isw.bookcafe.model.mo.User;

import java.util.List;

public interface ReviewDAO {

    public void addReview(Review review);

    public void updateReview(Review review);

    public void deleteReview(int reviewId);

    public Review getReviewById(int reviewId);

    public List<Review> getBookReviews(String bookISBN);

    public List<Review> getAllReviews();
}
