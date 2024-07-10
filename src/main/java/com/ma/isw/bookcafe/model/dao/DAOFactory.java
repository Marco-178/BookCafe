package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl.PSQLJDBCDAOFactory;
import com.ma.isw.bookcafe.model.dao.CookieImpl.CookieDAOFactory;

import java.util.Map;

public abstract class DAOFactory {

    // List of DAO types supported by the factory
    public static final String PSQLJDBCIMPL = "PostgreSQLJDBCImpl";
    public static final String COOKIEIMPL= "CookieImpl";

    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    public abstract UserDAO getUserDAO();
    public abstract ModeratorDAO getModeratorDAO();
    public abstract AdministratorDAO getAdministratorDAO();
    public abstract BookDAO getBookDAO();
    public abstract ClubDAO getClubDAO();
    public abstract EventDAO getEventDAO();
    public abstract ReviewDAO getReviewDAO();
    public abstract ThreadDAO getThreadDAO();
    public abstract UserMessageDAO getMessageDAO();
    public abstract SubscribedToDAO getSubscribedToDAO();

    public static DAOFactory getDAOFactory(String whichFactory,Map factoryParameters) {

        if (whichFactory.equals(PSQLJDBCIMPL)) {
            return new PSQLJDBCDAOFactory(factoryParameters);
        } else if (whichFactory.equals(COOKIEIMPL)) {
            return new CookieDAOFactory(factoryParameters);
        } else {
            return null;
        }
    }
}

