package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.Review;
import java.util.List;

public interface ReviewDAO {

    public Review addReview(int reviewId, int rating, String testo, String ISBNBook, int userId);

    public void updateReview(Review review);

    public void deleteReview(int reviewId);

    public Review getReviewById(int reviewId);

    public List<Review> getBookReviews(String bookISBN);

    public List<Review> getAllReviews();
}
