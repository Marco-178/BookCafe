package com.ma.isw.bookcafe.controller;

import com.ma.isw.bookcafe.model.dao.ClubDAO;
import com.ma.isw.bookcafe.model.dao.DAOFactory;
import com.ma.isw.bookcafe.model.dao.UserDAO;
import com.ma.isw.bookcafe.model.mo.Club;
import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.services.config.Configuration;
import com.ma.isw.bookcafe.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClubManagement {

    public static void viewClubsList(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        User loggedUser;
        DAOFactory daoFactory = null;
        List<Club> clubs;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            // recupero dal cookie l'utente che serve alla vista
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            // logica vera e propria della view
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            // transazione per subscribedTo a partire dal club e da loggedUser

            ClubDAO clubDAO = daoFactory.getClubDAO();
            clubs = clubDAO.getAllClubs();

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("clubsList", clubs);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "clubManagement/clubSearch");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void viewClub(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        User loggedUser;
        DAOFactory daoFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            ClubDAO clubDAO = daoFactory.getClubDAO();
            Club club = clubDAO.getClubById(Integer.parseInt(request.getParameter("clubId")));

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            ThreadManagement.viewThreadsList(request, response);

            request.setAttribute("club", club);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "clubManagement/clubView");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }
}
