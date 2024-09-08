package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.model.dao.*;
import com.ma.isw.bookcafe.services.config.Configuration;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


public class PSQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private Connection connection;

    public PSQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOPSQLJDBCImpl(connection);
    }

    @Override
    public ModeratorDAO getModeratorDAO() {
        return null;
    }

    @Override
    public AdministratorDAO getAdministratorDAO() {
        return null;
    }

    @Override
    public BookDAO getBookDAO() {
        return new BookDAOPSQLJDBCImpl(connection);
    }

    @Override
    public ClubDAO getClubDAO() {
        return new ClubDAOPSQLJDBCImpl(connection);
    }

    @Override
    public EventDAO getEventDAO() {
        return null;
    }

    @Override
    public ReviewDAO getReviewDAO() {
        return null;
    }

    @Override
    public ThreadDAO getThreadDAO() {
        return new ThreadDAOPSQLJDBCImpl(connection);
    }

    @Override
    public UserMessageDAO getMessageDAO() {
        return null;
    }

    @Override
    public SubscribedToDAO getSubscribedToDAO() {
        return null;
    }
}
