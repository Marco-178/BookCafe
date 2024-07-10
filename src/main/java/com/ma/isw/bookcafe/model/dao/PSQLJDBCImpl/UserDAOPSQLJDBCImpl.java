package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.model.dao.UserDAO;


public class UserDAOPSQLJDBCImpl implements UserDAO{

    Connection conn;

    public UserDAOPSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    // TODO per registrazione
    @Override
    public User addUser(Integer userId, String username, String email, String password, LocalDate subscriptionDate, LocalDate birthDate, String nation, String city, String urlProfilePicture, LocalDateTime lastAccess, Boolean banned, String userType, String biography) {
        throw new UnsupportedOperationException("Not supported.");
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
                user = read(resultSet);
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
        throw new UnsupportedOperationException("Not supported.");
    }

    public User read(ResultSet rs){
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
