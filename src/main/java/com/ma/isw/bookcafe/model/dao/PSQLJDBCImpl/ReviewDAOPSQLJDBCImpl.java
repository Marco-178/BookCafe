package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.model.dao.ReviewDAO;
import com.ma.isw.bookcafe.model.mo.Book;
import com.ma.isw.bookcafe.model.mo.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOPSQLJDBCImpl implements ReviewDAO {

    Connection conn;
    public ReviewDAOPSQLJDBCImpl(Connection conn) {this.conn = conn;}

    @Override
    public Review addReview(int reviewId, int rating, String testo, String ISBNBook, int userId) {
        return null;
    }

    @Override
    public void updateReview(Review review) {

    }

    @Override
    public void deleteReview(int reviewId) {

    }

    @Override
    public Review getReviewById(int reviewId) {
        return null;
    }

    @Override
    public List<Review> getBookReviews(String bookISBN) {
        List<Review> reviews = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM review WHERE isbn_book=?";
            ps= conn.prepareStatement(sql);
            ps.setString(1, bookISBN);
            rs = ps.executeQuery();

            while (rs.next()) {
                Review review = readReview(rs);

                reviews.add(review);
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return reviews;
    }

    @Override
    public List<Review> getAllReviews() {
        return List.of();
    }

    public static Review readReview(ResultSet rs) {
        Review review = new Review();
        try {
            int reviewId = rs.getInt("review_id");
            int rating = rs.getInt("rating");
            String testo = rs.getString("testo");
            String ISBNBook = rs.getString("ISBN_book");
            int userId = rs.getInt("user_id");
            boolean deleted = rs.getBoolean("deleted");

            review.setReviewId(reviewId);
            review.setRating(rating);
            review.setTesto(testo);
            review.setISBNBook(ISBNBook);
            review.setUserId(userId);
            review.setDeleted(deleted);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return review;
    }

}
