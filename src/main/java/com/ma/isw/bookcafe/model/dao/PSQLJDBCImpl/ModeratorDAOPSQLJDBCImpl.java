package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.model.dao.*;
import com.ma.isw.bookcafe.model.mo.Moderate;
import com.ma.isw.bookcafe.model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.SQLException;

public class ModeratorDAOPSQLJDBCImpl implements ModerateDAO, ModeratorDAO {
    Connection conn;

    public ModeratorDAOPSQLJDBCImpl(Connection conn) {this.conn = conn;}

    @Override
    public List<Moderate> getModerates(int clubId) {
        return List.of();
    }

    @Override
    public void addModerate() {

    }

    @Override
    public void deleteModerate() {

    }

    @Override
    public void addModerator(int userId, Date dataNomina) {

    }

    @Override
    public void updateModerator(User moderator) {

    }

    @Override
    public void deleteModerator(int userId) {

    }

    @Override
    public User getModeratorByUserId(int userId) {
        return null;
    }

    @Override
    public List<User> getModeratorsByClubId(int clubId) {
        List<User> moderators = new ArrayList<>();
        PreparedStatement ps = null;

        String query = "SELECT u.user_id, u.username, u.email, u.password, u.subscription_date, u.birth_date, u.nation, u.city, u.url_profile_picture, u.last_access, u.banned, u.user_type, u.biography, u.deleted " +
                "FROM users u " +
                "JOIN moderate m ON u.user_id = m.user_id " +
                "WHERE m.club_id = ? AND (u.user_type = 'moderator' OR u.user_type='admin')";

        try{
            ps = conn.prepareStatement(query);

            ps.setInt(1, clubId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSubscriptionDate(rs.getDate("subscription_date").toLocalDate());
                user.setBirthDate(rs.getDate("birth_date").toLocalDate());
                user.setNation(rs.getString("nation"));
                user.setCity(rs.getString("city"));
                user.setUrlProfilePicture(rs.getString("url_profile_picture"));
                user.setLastAccess(rs.getTimestamp("last_access").toLocalDateTime());
                user.setBanned(rs.getBoolean("banned"));
                user.setUserType(rs.getString("user_type"));
                user.setBiography(rs.getString("biography"));
                user.setDeleted(rs.getBoolean("deleted"));

                moderators.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return moderators;
    }


    @Override
    public List<User> getAllModerators() {
        return List.of();
    }
}
