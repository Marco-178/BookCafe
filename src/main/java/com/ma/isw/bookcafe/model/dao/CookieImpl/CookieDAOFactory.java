package com.ma.isw.bookcafe.model.dao.CookieImpl;

import com.ma.isw.bookcafe.model.dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class CookieDAOFactory extends DAOFactory {

    private Map factoryParameters;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            this.request=(HttpServletRequest) factoryParameters.get("request");
            this.response=(HttpServletResponse) factoryParameters.get("response");;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {}

    @Override
    public void rollbackTransaction() {}

    @Override
    public void closeTransaction() {}

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOCookieImpl(request,response);
    }

    @Override
    public ModeratorDAO getModeratorDAO() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public AdministratorDAO getAdministratorDAO() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public BookDAO getBookDAO() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ClubDAO getClubDAO() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public EventDAO getEventDAO() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ReviewDAO getReviewDAO() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ThreadDAO getThreadDAO() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public UserMessageDAO getMessageDAO() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public SubscribedToDAO getSubscribedToDAO() {
        throw new UnsupportedOperationException("Not supported");
    }


}
