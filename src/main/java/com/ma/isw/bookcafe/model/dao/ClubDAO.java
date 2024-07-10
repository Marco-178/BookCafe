package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.Club;
import java.util.List;
import java.util.Date;

public interface ClubDAO {

    public void addClub(Club club);

    public void updateClub(Club club);

    public void deleteClub(int clubId);

    public Club getClubById(int clubId);

    public List<Club> getAllClubs();
}
