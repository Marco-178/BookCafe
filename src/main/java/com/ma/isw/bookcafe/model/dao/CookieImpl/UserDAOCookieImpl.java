package com.ma.isw.bookcafe.model.dao.CookieImpl;

import com.ma.isw.bookcafe.model.mo.Review;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ma.isw.bookcafe.model.dao.UserDAO;
import com.ma.isw.bookcafe.model.mo.User;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public User addUser(User userToAdd) {

        User loggedUser = new User();
        loggedUser.setUserId(userToAdd.getUserId());
        loggedUser.setUsername(userToAdd.getUsername());
        loggedUser.setEmail(userToAdd.getEmail());
        loggedUser.setPassword(userToAdd.getPassword());
        loggedUser.setSubscriptionDate(userToAdd.getSubscriptionDate());
        loggedUser.setBirthDate(userToAdd.getBirthDate());
        loggedUser.setNation(userToAdd.getNation());
        loggedUser.setCity(userToAdd.getCity());
        loggedUser.setUrlProfilePicture(userToAdd.getUrlProfilePicture());
        loggedUser.setLastAccess(userToAdd.getLastAccess());
        loggedUser.setBanned(userToAdd.isBanned());
        loggedUser.setUserType(userToAdd.getUserType());
        loggedUser.setBiography(userToAdd.getBiography());

        String encodedUser = encode(loggedUser);
        String sanitizedEncodedUser = sanitizeCookieValue(encodedUser);

        Cookie cookie;
        cookie = new Cookie("loggedUser", sanitizedEncodedUser);
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;

    }

    private String sanitizeCookieValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String desanitizeCookieValue(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    @Override
    public void updateUser(User loggedUser) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void removeUser(Integer userId) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void deleteUser(User user) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void banUser(User user) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public User getLoggedUser() {

        Cookie[] cookies = request.getCookies();
        User loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    String desanitizedValue = desanitizeCookieValue(cookies[i].getValue());
                    if (!desanitizedValue.isEmpty()) {
                        loggedUser = decode(desanitizedValue);
                    }
                }
            }
        }

        return loggedUser;

    }

    @Override
    public User getUserById(Integer userId) {
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

    @Override
    public List<User> getReviewers(List<Review> reviews) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public LocalDateTime updateLastAccess(User user) {throw new UnsupportedOperationException("Not supported.");}

    private String encode(User loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getUserId() + "#"
                            + loggedUser.getUsername() + "#"
                            + loggedUser.getEmail() + "#"
                            + loggedUser.getPassword() + "#"
                            + loggedUser.getSubscriptionDate() + "#"
                            + loggedUser.getBirthDate() + "#"
                            + loggedUser.getNation() + "#"
                            + loggedUser.getCity() + "#"
                            + loggedUser.getUrlProfilePicture() + "#"
                            + loggedUser.getLastAccess() + "#"
                            + loggedUser.isBanned() + "#"
                            + loggedUser.getUserType() + "#"
                            + loggedUser.getBiography()+ "#"
                            + loggedUser.isDeleted();
        return encodedLoggedUser;

    }

    private User decode(String encodedLoggedUser) {

        User loggedUser = new User();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setUserId(Integer.parseInt(values[0]));
        loggedUser.setUsername(values[1]);
        loggedUser.setEmail(values[2]);
        loggedUser.setPassword(values[3]);
        if (!"null".equals(values[4])) {
            loggedUser.setSubscriptionDate(LocalDate.parse(values[4]));
        } else {
            loggedUser.setSubscriptionDate(null); // Or handle default value as per your logic
        }
        if (!"null".equals(values[5])) {
            loggedUser.setBirthDate(LocalDate.parse(values[5]));
        } else {
            loggedUser.setBirthDate(null); // Or handle default value as per your logic
        }
        loggedUser.setNation(values[6]);
        loggedUser.setCity(values[7]);
        loggedUser.setUrlProfilePicture(values[8]);
        if (!"null".equals(values[9])) {
            loggedUser.setLastAccess(LocalDateTime.parse(values[9]));
        } else {
            loggedUser.setLastAccess(null); // Or handle default value as per your logic
        }
        loggedUser.setBanned(Boolean.parseBoolean(values[10]));
        loggedUser.setUserType(values[11]);
        loggedUser.setBiography(values[12]);
        loggedUser.setDeleted(Boolean.parseBoolean(values[13]));


        return loggedUser;

    }

}

