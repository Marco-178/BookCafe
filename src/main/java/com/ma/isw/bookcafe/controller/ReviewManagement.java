package com.ma.isw.bookcafe.controller;

import com.ma.isw.bookcafe.model.dao.BookDAO;
import com.ma.isw.bookcafe.model.dao.DAOFactory;
import com.ma.isw.bookcafe.model.dao.ReviewDAO;
import com.ma.isw.bookcafe.model.dao.UserDAO;
import com.ma.isw.bookcafe.model.mo.Book;
import com.ma.isw.bookcafe.model.mo.Club;
import com.ma.isw.bookcafe.model.mo.Review;
import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.services.config.Configuration;
import com.ma.isw.bookcafe.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewManagement {

    private ReviewManagement(){

    }

    public static void writeReview(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        User loggedUser;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            // fase di scrittura sul DB
            String bookISBN = request.getParameter("bookISBN");
            Review reviewToAdd = new Review();
            reviewToAdd.setRating(Integer.parseInt(request.getParameter("rating")));
            reviewToAdd.setTesto(request.getParameter("reviewText"));
            reviewToAdd.setISBNBook(bookISBN);
            reviewToAdd.setUserId(loggedUser.getUserId());

            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            int modifyReviewId = Integer.parseInt(request.getParameter("modifyReview"));
            if(modifyReviewId >= 0) reviewDAO.deleteReview(modifyReviewId);
            reviewDAO.addReview(reviewToAdd);
            List<Review> reviews = reviewDAO.getBookReviews(bookISBN);

            // identico a viewBook

            BookDAO bookDAO = daoFactory.getBookDAO();
            Book book = bookDAO.getBookByISBN(bookISBN);

            UserDAO userDAO = daoFactory.getUserDAO();
            List<User> reviewers = userDAO.getReviewers(reviews);

            double meanReviews = calcReviewsMean(reviews);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("book", book);
            request.setAttribute("reviews", reviews);
            request.setAttribute("loggedUserReviewId", reviewToAdd.getReviewId());
            if(meanReviews != 0){
                request.setAttribute("meanReviews", meanReviews);
            }
            request.setAttribute("reviewers", reviewers);
            request.setAttribute("menuActiveLink", "Libro: " + book.getTitle());
            request.setAttribute("viewUrl", "bookSearchManagement/bookView");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void removeReview(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        User loggedUser;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            // fase di cancellazione sul DB
            String bookISBN = request.getParameter("bookISBN");

            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            reviewDAO.deleteReview(Integer.parseInt(request.getParameter("modifyReview")));
            List<Review> reviews = reviewDAO.getBookReviews(bookISBN);

            // identico a viewBook

            BookDAO bookDAO = daoFactory.getBookDAO();
            Book book = bookDAO.getBookByISBN(bookISBN);

            UserDAO userDAO = daoFactory.getUserDAO();
            List<User> reviewers = userDAO.getReviewers(reviews);

            int loggedUserReviewId = ReviewManagement.checkLoggedUserReview(reviews, loggedUser);
            double meanReviews = calcReviewsMean(reviews);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("book", book);
            request.setAttribute("reviews", reviews);
            request.setAttribute("loggedUserReviewId", loggedUserReviewId);
            if(meanReviews != 0){
                request.setAttribute("meanReviews", meanReviews);
            }
            request.setAttribute("reviewers", reviewers);
            request.setAttribute("menuActiveLink", "Libro: " + book.getTitle());
            request.setAttribute("viewUrl", "bookSearchManagement/bookView");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static double calcReviewsMean(List<Review> reviews){
        if (reviews.isEmpty()) {
            return 0;
        }
        double partialSum = 0;
        for (Review review : reviews) {
            partialSum += review.getRating();
        }
        return partialSum / reviews.size();
    }

    public static int checkLoggedUserReview(List<Review> reviews, User loggedUser) {
        for(Review review: reviews){
            if(review.getUserId() == loggedUser.getUserId()) return review.getReviewId();
        }
        return -1;
    }
}
