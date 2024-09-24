package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.controller.UserAccessManagement;
import com.ma.isw.bookcafe.model.dao.ReviewDAO;
import com.ma.isw.bookcafe.model.dao.UserDAO;
import com.ma.isw.bookcafe.model.dao.exception.DuplicatedUsernameException;
import com.ma.isw.bookcafe.model.dao.exception.InvalidBirthdateException;
import com.ma.isw.bookcafe.model.mo.Book;
import com.ma.isw.bookcafe.model.mo.Review;
import com.ma.isw.bookcafe.model.mo.User;

import java.sql.*;
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
    public void addReview(Review review) {
        PreparedStatement ps = null;

        try{
            String sql = "INSERT INTO review"
                    + "(rating, testo, isbn_book, user_id)"
                    + "VALUES (?,?,?,?)"
                    + "RETURNING review_id";

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setInt(i++, review.getRating());
            ps.setString(i++, review.getTesto());
            ps.setString(i++, review.getISBNBook());
            ps.setInt(i++, review.getUserId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int userReviewId = rs.getInt(1);
                        review.setReviewId(userReviewId);
                    }
                }
            }
        } catch(SQLException e){
            throw new RuntimeException("Errore durante l'inserimento della recensione nel database", e);
        }
    }

    @Override
    public void updateReview(Review review) {

    }

    @Override
    public void deleteReview(int reviewId) {
        PreparedStatement ps = null;
        try {
            String sql = " UPDATE review "
                    + "   SET deleted = true "
                    + "   WHERE review_id = ?";

            ps= conn.prepareStatement(sql);
            ps.setInt(1, reviewId);
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
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
                if(!review.isDeleted()) reviews.add(review);
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
