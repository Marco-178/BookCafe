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
import java.time.format.DateTimeFormatter;
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
            request.setAttribute("menuActiveLink", "Login");
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
            request.setAttribute("menuActiveLink", "Registrazione");
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
            request.setAttribute("menuActiveLink", "Login: Recupera password");
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

    public static void viewProfile(HttpServletRequest request, HttpServletResponse response){
        DAOFactory daoFactory = null;
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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UserDAO userDAO = daoFactory.getUserDAO();

            User viewedUser = new User();
            String parameterUserId = request.getParameter("profileId");
            if (!(parameterUserId == null || parameterUserId.isEmpty())) {
                int userId = Integer.parseInt(parameterUserId);
                viewedUser.setUser(userDAO.getUserById(userId));
            }
            else viewedUser.setUser(loggedUser);

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewedUser", viewedUser);
            request.setAttribute("formattedLastAccess", formatLocalDateTime(viewedUser.getLastAccess()));
            request.setAttribute("menuActiveLink", "Profilo di " + viewedUser.getUsername());
            request.setAttribute("viewUrl", "userAccessManagement/userProfile");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
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

    public static void signUp(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Logger logger = LogService.getApplicationLogger();
        String[] allowedExtensions = {"png", "jpeg", "jpg", "gif"};
        String relativeSavePath = "";
        String filename = "";
        String filePath = "";
        String username = (String)request.getAttribute("username");
        String filePathInDB = null;
        boolean isPictureUploaded = false;
        String contextPath = request.getServletContext().getRealPath("");

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            Part filePart = request.getPart("pictureUpload");
            if(filePart != null && filePart.getSize() > 0) isPictureUploaded = true;
            if (isPictureUploaded) {
                filename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                checkFileExtension(filename, allowedExtensions, filePart);
                relativeSavePath = "assets" + File.separator + "images" + File.separator + "profile";
                String absoluteSavePath = contextPath + relativeSavePath;
                System.out.println("context path: " + contextPath);

                // crea cartella se non esistente
                createDirectory(request, absoluteSavePath, filePart);
                String uniqueFilename = username + "_" + System.currentTimeMillis() + "_" + filename;
                filePath = absoluteSavePath + File.separator + uniqueFilename;
                filePart.write(filePath);
                filePathInDB = File.separator + relativeSavePath + File.separator + uniqueFilename;
                System.out.println("File salvato in: " + filePath);
                System.out.println("Percorso file sul DB: " + filePathInDB);
            }
            else {
                System.out.println("Nessun file file in upload");
            }

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UserDAO userDAO = daoFactory.getUserDAO();

            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();

            User loggedUser = new User();
            loggedUser.setUserId(-1);
            loggedUser.setUsername(username); // credo che getAttribute va bene se setti i parametri a partire dal Dispatcher, quindi sulla richiesta stessa; getParameter se viene dalla form.
            loggedUser.setEmail((String)request.getAttribute("email"));
            loggedUser.setPassword((String)request.getAttribute("password"));
            loggedUser.setSubscriptionDate(LocalDate.now());
            loggedUser.setBirthDate(LocalDate.parse((String)request.getAttribute("birthdate")));
            loggedUser.setNation((String)request.getAttribute("nation"));
            loggedUser.setCity((String)request.getAttribute("city"));
            if(isPictureUploaded) loggedUser.setUrlProfilePicture(filePathInDB);
            else {
                loggedUser.setUrlProfilePicture("/assets/images/profile/default_propic.gif");
                System.out.println("Setto default");
            }
            loggedUser.setLastAccess(LocalDateTime.now());
            loggedUser.setBanned(false);
            loggedUser.setUserType("normal");
            loggedUser.setBiography((String)request.getAttribute("biography")); // in realtà non c'è in signUp, si può fare dalla pagina profilo

            loggedUser = userDAO.addUser(loggedUser); // restituisce utente con userId assegnato dal DB
            User sessionLoggedUser = sessionUserDAO.addUser(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", sessionLoggedUser!=null);
            request.setAttribute("loggedUser", sessionLoggedUser);
            request.setAttribute("menuActiveLink", "Registrazione");
            request.setAttribute("viewUrl", "userAccessManagement/signUp");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            deleteFile(filePath);
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

    private static void createDirectory(HttpServletRequest request, String absoluteSavePath, Part filePart) throws IOException {
        boolean isDirectoryExistent = false;
        File fileSaveDir = new File(absoluteSavePath);
        System.out.println("file da caricare: " + filePart.getSize());
        if (!fileSaveDir.exists()) {
            isDirectoryExistent = fileSaveDir.mkdirs();
            if(!isDirectoryExistent){
                System.out.println("Errore DIRECTORY: " + absoluteSavePath);
                throw new IOException("Errore: impossibile creare la directory di salvataggio per l'immagine.");
            } else {
                System.out.println("Cartella creata con successo in: " + absoluteSavePath);
            }
        }
        else {
            System.out.println("La cartella esiste già: " + absoluteSavePath);
        }
    }

    private static void checkFileExtension(String filename, String[] allowedExtensions, Part filePart) {
        String fileExtension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase(); // controllo in caso sia senza estensione
        boolean validExtension = Arrays.asList(allowedExtensions).contains(fileExtension);
        if (!validExtension) {
            throw new IllegalArgumentException("Estensione file non supportata.");
        }

        String contentType = filePart.getContentType();
        boolean validContentType = contentType.startsWith("image/");

        if (!validContentType) {
            throw new IllegalArgumentException("Tipo di contenuto non supportato. Deve essere un'immagine.");
        }
    }

    private static void deleteFile(String filePath) {
        File savedFile = new File(filePath);
        if (savedFile.exists()) {
            boolean deleted = savedFile.delete();
            if (deleted) {
                System.out.println("File rimosso: " + filePath);
            } else {
                System.err.println("Impossibile rimuovere il file: " + filePath);
            }
        }
        else{
            System.out.println("File non rimovibile poichè non esiste: " + filePath);
        }
    }

    public static void modifyProfile(HttpServletRequest request, HttpServletResponse response){
        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Logger logger = LogService.getApplicationLogger();
        String[] allowedExtensions = {"png", "jpeg", "jpg", "gif"};
        String relativeSavePath = "";
        String filename = "";
        String filePath = "";
        String username = (String)request.getAttribute("username");
        String filePathInDB = null;
        boolean isPictureUploaded = false;
        String contextPath = request.getServletContext().getRealPath("");
        User viewedUser = new User();

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
            User loggedUser = sessionUserDAO.getLoggedUser();

            // prendo nota della foto da cancellare eventualmente (se è stata caricata una foto) e la cancello alla fine (cioè se la transazione avviene)
            String urlProfilePicToDelete = loggedUser.getUrlProfilePicture();
            System.out.println("To delete: "+ urlProfilePicToDelete);

            Part filePart = request.getPart("pictureUpload"); // l'upload foto è l'unico parametro privo di un valore di default nella form, quindi o viene caricato o è null!!
            if(filePart != null && filePart.getSize() > 0) isPictureUploaded = true;
            if (isPictureUploaded) {
                filename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                checkFileExtension(filename, allowedExtensions, filePart);
                relativeSavePath = "assets" + File.separator + "images" + File.separator + "profile";
                String absoluteSavePath = contextPath + relativeSavePath;

                // crea cartella se non esistente
                createDirectory(request, absoluteSavePath, filePart);
                String uniqueFilename = username + "_" + System.currentTimeMillis() + "_" + filename;
                filePath = absoluteSavePath + File.separator + uniqueFilename;
                filePart.write(filePath);
                filePathInDB = File.separator + relativeSavePath + File.separator + uniqueFilename;
                System.out.println("File salvato in: " + filePath);
                System.out.println("Percorso file sul DB: " + filePathInDB);
            }
            else {
                System.out.println("Nessun file in upload");
            }

            int profileId = Integer.parseInt((String) request.getAttribute("profileId"));
            if(profileId == loggedUser.getUserId()) {
                loggedUser.setEmail((String) request.getAttribute("email"));
                loggedUser.setPassword((String) request.getAttribute("password"));
                loggedUser.setBirthDate(LocalDate.parse((String) request.getAttribute("birthdate")));
                loggedUser.setNation((String) request.getAttribute("nation"));
                loggedUser.setCity((String) request.getAttribute("city"));
                if (isPictureUploaded) loggedUser.setUrlProfilePicture(filePathInDB);
                String userType = (String) request.getAttribute("userType");
                if (userType != null) loggedUser.setUserType(userType);
                loggedUser.setBiography((String) request.getAttribute("biography"));
                userDAO.updateUser(loggedUser);
                viewedUser.setUser(loggedUser);
            }
            else{
                viewedUser = userDAO.getUserById(profileId);
                viewedUser.setUserType((String) request.getAttribute("userType"));
                userDAO.updateUser(viewedUser);
            }

            sessionUserDAO.addUser(loggedUser);

            System.out.println("Nuovo url: "+ loggedUser.getUrlProfilePicture());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            // cancellazione foto vecchia (in caso venga cambiata)
            //System.out.println("cancellando nel percorso: " + contextPath + urlProfilePicToDelete.substring(1));
            if(isPictureUploaded) deleteFile(contextPath + urlProfilePicToDelete.substring(1)); // urlProfilePicToDelete comincia con un / con cui finisce anche contextPath, quindi per evitare doppie lo tolgo

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewedUser", viewedUser);
            request.setAttribute("formattedLastAccess", formatLocalDateTime(loggedUser.getLastAccess()));
            request.setAttribute("menuActiveLink", "Profilo di " + loggedUser.getUsername());
            request.setAttribute("viewUrl", "userAccessManagement/userProfile");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            System.out.println("Controller Errore: " + e);
            deleteFile(filePath);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            if(e instanceof IOException){
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

    public static void banProfile(HttpServletRequest request, HttpServletResponse response){
        DAOFactory daoFactory = null;
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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UserDAO userDAO = daoFactory.getUserDAO();

            User viewedUser = userDAO.getUserById(Integer.parseInt(request.getParameter("profileId")));
            userDAO.banUser(viewedUser);
            loggedUser.setBanned(true);

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("books", HomeManagement.getBooksData()); // siccome il logout restituisce la pagina della home devo caricare i libri
            request.setAttribute("menuActiveLink", "Home");
            request.setAttribute("viewUrl", "homeManagement/view");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
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

    public static void deleteProfile(HttpServletRequest request, HttpServletResponse response){
        DAOFactory daoFactory = null;
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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UserDAO userDAO = daoFactory.getUserDAO();

            //cancellazione logica da DB
            userDAO.deleteUser(loggedUser);
            //logout
            sessionUserDAO.deleteUser(null);

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("books", HomeManagement.getBooksData()); // siccome il logout restituisce la pagina della home devo caricare i libri
            request.setAttribute("menuActiveLink", "Home");
            request.setAttribute("viewUrl", "homeManagement/view");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
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
            request.setAttribute("menuActiveLink", "Login: Recupera password");
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
        String errorMessage = null;

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
                errorMessage = "Username o password errati!";
                loggedUser=null;
            }
            else if(user.isBanned()){
                sessionUserDAO.deleteUser(null);
                errorMessage = "L'utente " + user.getUsername() + "e' stato cancellato dall'amministratore.";
                loggedUser=null;
            }
            else if(user.isDeleted()){
                sessionUserDAO.deleteUser(null);
                errorMessage = "Questo account è stato cancellato dall'utente.";
                loggedUser=null;
            }
            else{
                userDAO.updateLastAccess(user);
                loggedUser = sessionUserDAO.addUser(user);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("menuActiveLink", "Home");
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
            request.setAttribute("books", HomeManagement.getBooksData()); // siccome il logout restituisce la pagina della home devo caricare i libri
            request.setAttribute("menuActiveLink", "Home");
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

    public static String formatLocalDateTime(LocalDateTime localDateTimeToFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedLocalDateTime = localDateTimeToFormat.format(formatter);
        return formattedLocalDateTime;
    }

}
