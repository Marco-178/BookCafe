package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.dao.exception.InvalidBirthdateException;
import com.ma.isw.bookcafe.model.dao.exception.DuplicatedUsernameException;
import com.ma.isw.bookcafe.model.mo.Review;
import com.ma.isw.bookcafe.model.mo.User;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserDAO {

    public User addUser(User userToAdd) throws DuplicatedUsernameException, InvalidBirthdateException;

    public void deleteUser(User user);

    void banUser(User user);

    public void updateUser(User user);

    public void removeUser(Integer userId);

    public User getLoggedUser();

    public User getUserById(Integer userId);

    public User getByUsername(String username);

    public List<User> getAllUsers();

    List<User> getReviewers(List<Review> reviews);

    public LocalDateTime updateLastAccess(User user);
}
