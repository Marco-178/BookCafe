package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.model.dao.ClubDAO;
import com.ma.isw.bookcafe.model.mo.Club;
import com.ma.isw.bookcafe.model.mo.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ClubDAOPSQLJDBCImpl implements ClubDAO {

    Connection conn;

    public ClubDAOPSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void addClub(Club club) {

    }

    @Override
    public void updateClub(Club club) {

    }

    @Override
    public void deleteClub(int clubId) {

    }

    @Override
    public Club getClubById(int clubId) {
        PreparedStatement ps;
        Club club = null;

        try {
            String sql
                    = " SELECT * "
                    + "   FROM club "
                    + " WHERE "
                    + "   club_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, clubId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String clubName = rs.getString("club_name");
                LocalDate creationDate = rs.getDate("creation_date").toLocalDate();
                String description = rs.getString("description");
                boolean deleted = rs.getBoolean("deleted");

                // Inizializzazione dell'oggetto Club e impostazione dei valori
                club = new Club();
                club.setClubId(clubId);
                club.setClubName(clubName);
                club.setCreationDate(creationDate);
                club.setDescription(description);
                club.setDeleted(deleted);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return club;
    }

    @Override
    public List<Club> getAllClubs() {
        List<Club> clubs = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM club";
            ps= conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int clubId = rs.getInt("club_id");
                String clubName = rs.getString("club_name");
                LocalDate creationDate = rs.getDate("creation_date").toLocalDate();
                String description = rs.getString("description");
                boolean deleted = rs.getBoolean("deleted");

                // Inizializzazione dell'oggetto Club e impostazione dei valori
                Club club = new Club();
                club.setClubId(clubId);
                club.setClubName(clubName);
                club.setCreationDate(creationDate);
                club.setDescription(description);
                club.setDeleted(deleted);

                clubs.add(club);
            }

        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return clubs;
    }
}
