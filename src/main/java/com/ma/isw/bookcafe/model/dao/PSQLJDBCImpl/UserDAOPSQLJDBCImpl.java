package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ma.isw.bookcafe.model.dao.exception.DuplicatedUsernameException;
import com.ma.isw.bookcafe.model.dao.exception.InvalidBirthdateException;
import com.ma.isw.bookcafe.model.mo.Review;
import com.ma.isw.bookcafe.model.mo.Thread;
import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.model.dao.UserDAO;


public class UserDAOPSQLJDBCImpl implements UserDAO{

    Connection conn;

    public UserDAOPSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public User addUser(Integer userId, String username, String email, String password, LocalDate subscriptionDate, LocalDate birthDate, String nation, String city, String urlProfilePicture, LocalDateTime lastAccess, Boolean banned, String userType, String biography) throws DuplicatedUsernameException, InvalidBirthdateException {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setSubscriptionDate(subscriptionDate);
        user.setBirthDate(birthDate);
        user.setNation(nation);
        user.setCity(city);
        user.setUrlProfilePicture(urlProfilePicture);
        user.setLastAccess(lastAccess);
        user.setBanned(banned);
        user.setUserType(userType);
        user.setBiography(biography);

        PreparedStatement ps = null;

        try{
            String sql = "INSERT INTO users "
                    + "(username, email, password, subscription_date, birth_date, nation, city, "
                    + " url_profile_picture, last_access, banned, user_type, biography, deleted) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    + "RETURNING user_id"; // siccome il DB usa ID auto-incrementali devo poi usare getGeneratedKeys() per recuperarlo e metterlo nell'oggetto user restituito

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // per il RETURNING
            int i = 1;
            ps.setString(i++, user.getUsername());
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getPassword());
            ps.setDate(i++, java.sql.Date.valueOf(user.getSubscriptionDate()));
            ps.setDate(i++, java.sql.Date.valueOf(user.getBirthDate()));
            ps.setString(i++, user.getNation());
            ps.setString(i++, user.getCity());
            ps.setString(i++, user.getUrlProfilePicture());
            ps.setTimestamp(i++, java.sql.Timestamp.valueOf(user.getLastAccess()));
            ps.setBoolean(i++, user.isBanned());
            ps.setString(i++, user.getUserType());
            ps.setString(i++, user.getBiography());
            ps.setBoolean(i++, user.isDeleted());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int loggedUserId = rs.getInt(1);
                        user.setUserId(loggedUserId);
                    }
                }
            }
        }
        catch(SQLException e){
            if (e.getMessage().contains("\"users_username_key\"")) {
                throw new DuplicatedUsernameException("Username already exists.", e);
            } else if (e.getMessage().contains("users_birth_date_check")) {
                throw new InvalidBirthdateException("Errore: Violazione del vincolo users_birth_date_check", e);
            }
            else throw new RuntimeException("Errore durante l'inserimento dell'utente nel database", e);
        }

        return user;
    }

    // TODO Per modificare dettagli profilo utente
    @Override
    public void updateUser(User user) {
        throw new UnsupportedOperationException("Not supported.");
    }

    // TODO dove però metto solo deleted=True
    @Override
    public void deleteUser(Integer userId) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public User getLoggedUser() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public User getByUserId(Integer userId) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public User getByUsername(String username) {
        PreparedStatement ps;
        User user = null;

        try {

            String sql
                    = " SELECT * "
                    + " FROM users "
                    + " WHERE "
                    + "   username = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user = UserDAOPSQLJDBCImpl.read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM users";

            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                User user = UserDAOPSQLJDBCImpl.read(rs);
                users.add(user);
            }

        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public List<User> getReviewers(List<Review> reviews) {
        List<User> users = getAllUsers();
        List<User> reviewers = new ArrayList<>();
        for (Review review : reviews) {
            for (User user : users) {
                if (user.getUserId() == review.getUserId()) {
                    reviewers.add(user);
                    break;
                }
            }
        }
        return reviewers;
    }

    public static User read(ResultSet rs){
        User user = new User();

        try{
            user.setUserId(rs.getInt("user_id"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setUsername(rs.getString("username"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setEmail(rs.getString("email"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setPassword(rs.getString("password"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setSubscriptionDate(rs.getObject("subscription_date", LocalDate.class));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setBirthDate(rs.getObject("birth_date", LocalDate.class));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setNation(rs.getString("nation"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setCity(rs.getString("city"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setUrlProfilePicture(rs.getString("url_profile_picture"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setLastAccess(rs.getObject("last_access", LocalDateTime.class));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setBanned(rs.getBoolean("banned"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setUserType(rs.getString("user_type"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            user.setBiography(rs.getString("biography"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return user;
    }
}
