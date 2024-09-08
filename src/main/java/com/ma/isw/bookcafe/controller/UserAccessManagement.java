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
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            sessionDAOFactory.commitTransaction();

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

    public static void viewPasswordRecovery(HttpServletRequest request, HttpServletResponse response) {
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
            request.setAttribute("viewUrl", "userAccessManagement/passwordRecovery");
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
        String filename = new String();
        String relativeSavePath = new String();
        String[] allowedExtensions = {"png", "jpeg", "jpg", "gif"};

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            Part filePart = request.getPart("pictureUpload");
            if (filePart != null && filePart.getSize() > 0) {
                filename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // TODO in teoria devo dare un nome incrementale per tutte le immagini profilo salvate, così evito problemi di sovrapposizione nomi (potrei mettere l'id dell'utente)
                String fileExtension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                boolean validExtension = Arrays.asList(allowedExtensions).contains(fileExtension);
                if (!validExtension) {
                    throw new IllegalArgumentException("Estensione file non supportata.");
                }

                relativeSavePath = File.separator + "assets" + File.separator + "images" + File.separator + "profile";
                String contextPath = request.getServletContext().getRealPath("");
                String absoluteSavePath = contextPath + relativeSavePath;

                // crea cartella se non esistente
                boolean isCreated = false;
                File fileSaveDir = new File(absoluteSavePath);
                if (!fileSaveDir.exists()) {
                    isCreated = fileSaveDir.mkdirs();
                }
                if(!isCreated) throw new IOException();

                String filePath = absoluteSavePath + File.separator + filename;
                filePart.write(filePath);
                System.out.println("path:"+filePath);
            }

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UserDAO userDAO = daoFactory.getUserDAO();

            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();

            User loggedUser = new User();
            loggedUser.setUserId(-1);
            loggedUser.setUsername((String)request.getAttribute("username")); // TODO perchè usato getAttribute e non getParameter?
            loggedUser.setEmail((String)request.getAttribute("email"));
            loggedUser.setPassword((String)request.getAttribute("password"));
            System.out.println("sub"+request.getAttribute("password"));
            loggedUser.setSubscriptionDate(LocalDate.now());
            loggedUser.setBirthDate(LocalDate.parse((String)request.getAttribute("birthdate")));
            loggedUser.setNation((String)request.getAttribute("nation"));
            loggedUser.setCity((String)request.getAttribute("city"));
            loggedUser.setUrlProfilePicture(relativeSavePath+filename);
            loggedUser.setLastAccess(LocalDateTime.now());
            loggedUser.setBanned(false);
            loggedUser.setUserType("normal");
            loggedUser.setBiography((String)request.getAttribute("biography"));

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
                    loggedUser.getBiography()
            );
            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            String lastPage = (String) request.getAttribute("currentPage");

            request.setAttribute("loggedOn", sessionLoggedUser!=null);
            request.setAttribute("loggedUser", sessionLoggedUser);
            request.setAttribute("books", HomeManagement.getBooksData()); // per la home, in generale qualsiasi attributo aggiuntivo che serve per una view deve essere settata anche sul logout
            //request.setAttribute("viewUrl", lastPage);
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
            else if(e instanceof IOException){
                request.setAttribute("errorMessage", "Salvataggio file non riuscito.");
                request.setAttribute("ExceptionType", "IOException");
                request.setAttribute("viewUrl", "userAccessManagement/signUp");
            }
            else if(e instanceof IllegalArgumentException){
                request.setAttribute("errorMessage", "Il file deve essere un'immagine PNG, JPEG o GIF.");
                request.setAttribute("ExceptionType", "IllegalArgumentException");
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

    public static void recoverPassword(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            // TODO:
            //  1.confronto attributo ricevuto da "password dimetnicata" e confronto con mail nel database
            //  2.generazione di token corrispondente all'utente trovato e invio mail
            //  3.salvataggio token con TTL e cancellazione automatica
            //  4.verifica validità token al click del link inviato per mail
            //  5.sollevamento eccezioni: 1.quando mail non è nel db, 2.quando il token è scaduto
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.getLoggedUser();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "userAccessManagement/passwordRecovery");
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

            if (user == null || !user.getPassword().equals(password)) {
                sessionUserDAO.deleteUser(null);
                applicationMessage = "Username e password errati!";
                loggedUser=null;
            } else {
                loggedUser = sessionUserDAO.addUser(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getSubscriptionDate(), user.getBirthDate(), user.getNation(), user.getCity(), user.getUrlProfilePicture(), user.getLastAccess(), user.isBanned(), user.getUserType(), user.getBiography());
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("books", HomeManagement.getBooksData());
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "homeManagement/view");

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
            request.setAttribute("books", HomeManagement.getBooksData()); // per la home, in generale qualsiasi attributo aggiuntivo che serve per una view deve essere settata anche sul logout
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

}
