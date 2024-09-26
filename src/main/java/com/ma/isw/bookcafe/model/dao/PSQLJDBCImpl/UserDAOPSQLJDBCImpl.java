package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import java.sql.*;

import java.sql.Date;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ma.isw.bookcafe.model.dao.exception.DuplicatedUsernameException;
import com.ma.isw.bookcafe.model.dao.exception.InvalidBirthdateException;
import com.ma.isw.bookcafe.model.mo.Review;
import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.model.dao.UserDAO;


public class UserDAOPSQLJDBCImpl implements UserDAO{

    Connection conn;

    public UserDAOPSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public User addUser(User userToAdd) throws DuplicatedUsernameException, InvalidBirthdateException {
        User user = new User();
        user.setUser(userToAdd);

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

    // mette solo deleted=True (cancellazione logica)
    @Override
    public void deleteUser(User user) {
        PreparedStatement ps = null;
        user.setDeleted(true);
        try {
            String sql = " UPDATE users "
                    + "   SET deleted = ? "
                    + "   WHERE user_id = ?";

            ps= conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setInt(2, user.getUserId());
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void banUser(User user) {
        PreparedStatement ps = null;
        user.setBanned(true);
        try {
            String sql = " UPDATE users "
                    + "   SET banned = ? "
                    + "   WHERE user_id = ?";

            ps= conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setInt(2, user.getUserId());
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(User userToUpdate){
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE users SET "
                    + "username = ?, email = ?, password = ?, subscription_date = ?, birth_date = ?, "
                    + "nation = ?, city = ?, url_profile_picture = ?, last_access = ?, banned = ?, "
                    + "user_type = ?, biography = ?, deleted = ? "
                    + "WHERE user_id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, userToUpdate.getUsername());
            ps.setString(i++, userToUpdate.getEmail());
            ps.setString(i++, userToUpdate.getPassword());
            ps.setDate(i++, java.sql.Date.valueOf(userToUpdate.getSubscriptionDate()));
            ps.setDate(i++, java.sql.Date.valueOf(userToUpdate.getBirthDate()));
            ps.setString(i++, userToUpdate.getNation());
            ps.setString(i++, userToUpdate.getCity());
            ps.setString(i++, userToUpdate.getUrlProfilePicture());
            ps.setTimestamp(i++, java.sql.Timestamp.valueOf(userToUpdate.getLastAccess()));
            ps.setBoolean(i++, userToUpdate.isBanned());
            ps.setString(i++, userToUpdate.getUserType());
            ps.setString(i++, userToUpdate.getBiography());
            ps.setBoolean(i++, userToUpdate.isDeleted());
            ps.setInt(i++, userToUpdate.getUserId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Errore: Nessun utente aggiornato, l'ID dell'utente potrebbe non esistere.");
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    // cancella l'utente dal DB
    @Override
    public void removeUser(Integer userId) {
        PreparedStatement ps = null;
        try {
            String sql = " DELETE FROM users"
                    + "   WHERE user_id = ?";

            ps= conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getLoggedUser() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public User getUserById(Integer userId) {
        PreparedStatement ps;
        User user = null;

        try {

            String sql
                    = " SELECT * "
                    + " FROM users "
                    + " WHERE "
                    + "   user_id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

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
        Map<Integer, User> userMap = new HashMap<>();

        // Usando una HashMap la complessità computazionale si riduce a O(n)
        for (User user : users) {
            userMap.put(user.getUserId(), user);
        }

        List<User> reviewers = new ArrayList<>();
        // Siccome a ciclare una lista si usa un Iterator, il modo più sicuro per rimuovere elementi dalla lista ed evitare errori di concorrenza (tra il ciclo e la rimozione) è usare l'Iterator
        Iterator<Review> reviewIterator = reviews.iterator();

        while (reviewIterator.hasNext()) {
            Review review = reviewIterator.next();
            User user = userMap.get(review.getUserId());

            if (user != null && !user.isBanned()) {
                reviewers.add(user);
            } else if (user != null && user.isBanned()) {
                reviewIterator.remove();
            }
        }

        return reviewers;
    }


    @Override
    public LocalDateTime updateLastAccess(User user) {
        PreparedStatement ps = null;
        LocalDateTime lastAccess = LocalDateTime.now();
        user.setLastAccess(lastAccess);
        try {
            String sql = " UPDATE users "
                    + "   SET last_access = ? "
                    + "   WHERE user_id = ?";

            ps= conn.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(lastAccess));
            ps.setInt(2, user.getUserId());
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return LocalDateTime.now();
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

        try{
            user.setDeleted(rs.getBoolean("deleted"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return user;
    }
}
