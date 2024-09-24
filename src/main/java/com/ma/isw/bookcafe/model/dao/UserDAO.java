package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.dao.exception.InvalidBirthdateException;
import com.ma.isw.bookcafe.model.dao.exception.DuplicatedUsernameException;
import com.ma.isw.bookcafe.model.mo.Review;
import com.ma.isw.bookcafe.model.mo.User;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserDAO {

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
            String biography
    ) throws DuplicatedUsernameException, InvalidBirthdateException;

    public void updateUser(User user);

    public void deleteUser(Integer userId);

    public User getLoggedUser();

    public User getByUserId(Integer userId);

    public User getByUsername(String username);

    public List<User> getAllUsers();

    List<User> getReviewers(List<Review> reviews);
}
