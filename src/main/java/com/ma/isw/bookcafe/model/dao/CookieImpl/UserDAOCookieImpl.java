package com.ma.isw.bookcafe.model.dao.CookieImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ma.isw.bookcafe.model.dao.UserDAO;
import com.ma.isw.bookcafe.model.mo.User;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class UserDAOCookieImpl implements UserDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public User addUser(
            Integer userId,
            String username,
            String email,
            String password,
            LocalDate subscriptionDate,
            LocalDate birthDate,
            String nation,
            String city,
            String urlProfilePicture,
            LocalDateTime lastAccess,
            Boolean banned,
            String userType,
            String biography) {

        User loggedUser = new User();
        loggedUser.setUserId(userId);
        loggedUser.setUsername(username);
        loggedUser.setSubscriptionDate(subscriptionDate);
        loggedUser.setUserType(userType);

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;

    }

    @Override
    public void updateUser(User loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void deleteUser(Integer userId) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public User getLoggedUser() {

        Cookie[] cookies = request.getCookies();
        User loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;

    }

    @Override
    public User getByUserId(Integer userId) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public User getByUsername(String username) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public List<User> getAllUsers() {
        throw new UnsupportedOperationException("Not supported.");
    }

    private String encode(User loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getUserId() + "#" + loggedUser.getUsername() + "#" + loggedUser.getUserType();
        return encodedLoggedUser;

    }

    private User decode(String encodedLoggedUser) {

        User loggedUser = new User();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setUserId(Integer.parseInt(values[0]));
        loggedUser.setUsername(values[1]);
        loggedUser.setUserType(values[2]);

        return loggedUser;

    }

}

