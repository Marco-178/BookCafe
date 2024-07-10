package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.Moderator;
import java.util.List;
import java.util.Date;

public interface ModeratorDAO {

    public void addModerator(int userId, Date dataNomina);

    public void updateModerator(Moderator moderator);

    public void deleteModerator(int userId);

    public Moderator getModeratorByUserId(int userId);

    public List<Moderator> getAllModerators();
}
