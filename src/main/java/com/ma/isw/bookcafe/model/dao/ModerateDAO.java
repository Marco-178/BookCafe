package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.Moderate;
import java.util.List;

public interface ModerateDAO {
    public List<Moderate> getModerates(int clubId);

    public void addModerate();

    public void deleteModerate();
}
