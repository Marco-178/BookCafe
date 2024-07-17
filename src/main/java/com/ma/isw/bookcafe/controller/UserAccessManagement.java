package com.ma.isw.bookcafe.controller;

import com.ma.isw.bookcafe.model.dao.DAOFactory;
import com.ma.isw.bookcafe.model.dao.UserDAO;
import com.ma.isw.bookcafe.model.dao.exception.DuplicatedUsernameException;
import com.ma.isw.bookcafe.model.dao.exception.InvalidBirthdateException;
import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.services.config.Configuration;
import com.ma.isw.bookcafe.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserAccessManagement {

    private UserAccessManagement() {
    }

    public static void viewLogin(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "userAccessManagement/login");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void viewSignUp(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters); // TODO fare transazione corretta
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("signedUp",false);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "userAccessManagement/signUp");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void logon(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

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

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.getByUsername(username);

            // TODO aggiungere transazione per aggiornare su DB l'ultimo accesso al profilo

            if (user == null || !user.getPassword().equals(password)) {
                sessionUserDAO.deleteUser(null);
                applicationMessage = "Username e password errati!";
                loggedUser=null;
            } else {
                loggedUser = sessionUserDAO.addUser(
                user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getSubscriptionDate(),
                        user.getBirthDate(),
                        user.getNation(),
                        user.getCity(),
                        user.getUrlProfilePicture(),
                        user.getLastAccess(),
                        user.isBanned(),
                        user.getUserType(),
                        user.getBiography()
                );
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "userAccessManagement/login");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            sessionUserDAO.deleteUser(null);

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void signUp(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UserDAO userDAO = daoFactory.getUserDAO();

            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();

            User loggedUser = new User();
            loggedUser.setUserId(-1);
            loggedUser.setUsername(request.getParameter("username"));
            loggedUser.setEmail(request.getParameter("email"));
            loggedUser.setPassword(request.getParameter("password"));
            loggedUser.setSubscriptionDate(LocalDate.now());
            loggedUser.setBirthDate(LocalDate.parse(request.getParameter("birthdate")));
            loggedUser.setNation(request.getParameter("nation"));
            loggedUser.setCity(request.getParameter("city"));
            loggedUser.setUrlProfilePicture(request.getParameter("urlProfilePicture"));
            loggedUser.setLastAccess(LocalDateTime.now());
            loggedUser.setBanned(false);
            loggedUser.setUserType("normal");
            loggedUser.setBiography(request.getParameter("biography"));

            loggedUser = userDAO.addUser(
                    loggedUser.getUserId(),
                    loggedUser.getUsername(),
                    loggedUser.getEmail(),
                    loggedUser.getPassword(),
                    loggedUser.getSubscriptionDate(),
                    loggedUser.getBirthDate(),
                    loggedUser.getNation(),
                    loggedUser.getCity(),
                    loggedUser.getUrlProfilePicture(),
                    loggedUser.getLastAccess(),
                    loggedUser.isBanned(),
                    loggedUser.getUserType(),
                    loggedUser.getBiography()
            );

            User sessionLoggedUser = sessionUserDAO.addUser(
                    loggedUser.getUserId(),
                    loggedUser.getUsername(),
                    loggedUser.getEmail(),
                    loggedUser.getPassword(),
                    loggedUser.getSubscriptionDate(),
                    loggedUser.getBirthDate(),
                    loggedUser.getNation(),
                    loggedUser.getCity(),
                    loggedUser.getUrlProfilePicture(),
                    loggedUser.getLastAccess(),
                    loggedUser.isBanned(),
                    loggedUser.getUserType(),
                    loggedUser.getBiography());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", sessionLoggedUser!=null);
            request.setAttribute("loggedUser", sessionLoggedUser);
            request.setAttribute("viewUrl", "userAccessManagement/signUp");

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            if(e instanceof DuplicatedUsernameException){
                request.setAttribute("errorMessage", "Il nome utente è già registrato. Riprovare con un altro nome utente.");
                request.setAttribute("ExceptionType", "DuplicatedUsernameException");
                request.setAttribute("viewUrl", "userAccessManagement/signUp");
            }
            else if(e instanceof InvalidBirthdateException){
                request.setAttribute("errorMessage", "La data di nascita inserita non è valida. Inserire la vera data di nascita.");
                request.setAttribute("ExceptionType", "InvalidBirthdateException");
                request.setAttribute("viewUrl", "userAccessManagement/signUp");
            }
            else throw new RuntimeException(e);
        }
        finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

}
