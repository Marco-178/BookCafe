package com.ma.isw.bookcafe.controller;

import com.ma.isw.bookcafe.model.dao.ClubDAO;
import com.ma.isw.bookcafe.model.dao.EventDAO;
import com.ma.isw.bookcafe.model.dao.DAOFactory;
import com.ma.isw.bookcafe.model.dao.UserDAO;
import com.ma.isw.bookcafe.model.mo.Club;
import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.model.mo.Event;
import com.ma.isw.bookcafe.services.config.Configuration;
import com.ma.isw.bookcafe.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventManagement {

    private static List<Event> eventsList;

    public static void viewEventsList(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        User loggedUser;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);

            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            EventDAO eventDAO = daoFactory.getEventDAO();
            eventsList = eventDAO.getAllEvents();

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("eventsList", eventsList);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("menuActiveLink", "Lista eventi");
            request.setAttribute("viewUrl", "eventManagement/eventSearch");

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

    public static void viewEvent(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        User loggedUser;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);

            int eventId = Integer.parseInt(request.getParameter("eventId"));

            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            EventDAO eventDAO = daoFactory.getEventDAO();
            Event event = eventDAO.getEventById(eventId);

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("event", event);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("menuActiveLink", "Evento: " + event.getEventName());
            request.setAttribute("viewUrl", "eventManagement/eventView");

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
