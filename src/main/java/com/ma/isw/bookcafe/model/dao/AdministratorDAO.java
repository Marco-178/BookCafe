package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.Administrator;
import java.util.List;
import java.util.Date;

public interface AdministratorDAO {

    public void addAdministrator(int userId, Date dataNomina);

    public void updateAdministrator(Administrator administrator);

    public void deleteAdministrator(int userId);

    public Administrator getAdministratorByUserId(int userId);

    public List<Administrator> getAllAdministrators();
}
