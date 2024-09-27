package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.User;

import java.util.List;
import java.util.Date;

public interface ModeratorDAO {

    public void addModerator(int userId, Date dataNomina);

    public void updateModerator(User moderator);

    public void deleteModerator(int userId);

    public User getModeratorByUserId(int userId);

    public List<User> getModeratorsByClubId(int clubId);

    public List<User> getAllModerators();
}
