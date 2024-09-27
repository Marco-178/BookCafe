package com.ma.isw.bookcafe.controller;

import com.ma.isw.bookcafe.model.dao.*;
import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.model.mo.Thread;
import com.ma.isw.bookcafe.model.mo.Message;
import com.ma.isw.bookcafe.services.config.Configuration;
import com.ma.isw.bookcafe.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tags.shaded.org.apache.xalan.templates.NamespaceAlias;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.ma.isw.bookcafe.controller.UserAccessManagement.formatLocalDateTime;

public class ThreadManagement {

    public static void viewThreadsList(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory daoFactory = null;
        List<Thread> threads;
        List<User> users;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);

            int clubId = Integer.parseInt(request.getParameter("clubId"));

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            ThreadDAO threadDAO = daoFactory.getThreadDAO();
            threads = threadDAO.getAllThreads(clubId);
            List<String> threadsformattedCreationTimestamps = threadDAO.getThreadsFormattedCreationTimestamps(threads);

            // TODO getThreadsTotalMessages

            List<Integer> threadsTotalMessages = ThreadManagement.getThreadsTotalMessages(threadDAO, threads);

            UserDAO userDAO = daoFactory.getUserDAO();
            users = userDAO.getAllUsers();

            daoFactory.commitTransaction();

            request.setAttribute("threadsList", threads);
            request.setAttribute("threadsTotalMessages", threadsTotalMessages);
            request.setAttribute("threadsformattedCreationTimestamps", threadsformattedCreationTimestamps);
            request.setAttribute("usersList", users);

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

    private static List<Integer> getThreadsTotalMessages(ThreadDAO threadDAO, List<Thread> threads) {
        List<Integer> threadsTotalMessages = new ArrayList<>();
        for (Thread thread : threads) {
            int messageCount = threadDAO.countTotalMessages(thread.getThreadId());
            threadsTotalMessages.add(messageCount);
        }
        return threadsTotalMessages;
    }

    public static void viewThread(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        List<Message> messages;
        List<User> chatters;

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

            int threadId = Integer.parseInt(request.getParameter("threadId"));
            MessageDAO messageDAO = daoFactory.getMessageDAO();
            messages = messageDAO.getThreadMessages(threadId);

            ThreadDAO threadDAO = daoFactory.getThreadDAO();
            Thread thread = threadDAO.getThreadById(threadId);

            UserDAO userDAO = daoFactory.getUserDAO();
            chatters = userDAO.getChatters(messages);
            List<String> formattedCreationTimestamps = threadDAO.getFormattedCreationTimestamps(messages);
            User threadUser = userDAO.getUserById(thread.getUserId());

            int clubId = Integer.parseInt((String)request.getParameter("clubId"));
            ModeratorDAO moderatorDAO = daoFactory.getModeratorDAO();
            List <User> clubMods = moderatorDAO.getModeratorsByClubId(clubId);

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("thread", thread);
            request.setAttribute("threadUser", threadUser);
            request.setAttribute("messages", messages);
            request.setAttribute("chatters", chatters);
            request.setAttribute("clubMods", clubMods);
            request.setAttribute("formattedLastReply", formatLocalDateTime(thread.getTimestampLastReply()));
            request.setAttribute("threadFormattedCreationTimestamp", formatLocalDateTime(thread.getCreationTimestamp()));
            request.setAttribute("formattedCreationTimestamps", formattedCreationTimestamps);
            request.setAttribute("clubId", clubId);
            request.setAttribute("menuActiveLink", "Discussione: " + thread.getTitle());
            request.setAttribute("viewUrl", "threadManagement/threadView");
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

    public static void writeComment(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        List<Message> messages;
        List<User> chatters;

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

            int threadId = Integer.parseInt(request.getParameter("threadId"));
            // salvo messaggio sul DB
            MessageDAO messageDAO = daoFactory.getMessageDAO();
            Message message = new Message();
            message.setCreationTimestamp(LocalDateTime.now());
            message.setContent(request.getParameter("commentTextArea"));
            message.setThreadId(threadId);
            message.setUserId(loggedUser.getUserId());
            message.setDeleted(false);
            messageDAO.addMessage(message);

            // vista
            messages = messageDAO.getThreadMessages(threadId);

            ThreadDAO threadDAO = daoFactory.getThreadDAO();
            Thread thread = threadDAO.getThreadById(threadId);

            // TODO aggiornare lastReply del thread

            UserDAO userDAO = daoFactory.getUserDAO();
            chatters = userDAO.getChatters(messages);
            List<String> formattedCreationTimestamps = threadDAO.getFormattedCreationTimestamps(messages);
            User threadUser = userDAO.getUserById(thread.getUserId());

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("thread", thread);
            request.setAttribute("threadUser", threadUser);
            request.setAttribute("messages", messages);
            request.setAttribute("chatters", chatters);
            request.setAttribute("formattedLastReply", formatLocalDateTime(thread.getTimestampLastReply()));
            request.setAttribute("threadFormattedCreationTimestamp", formatLocalDateTime(thread.getCreationTimestamp()));
            request.setAttribute("formattedCreationTimestamps", formattedCreationTimestamps);
            request.setAttribute("menuActiveLink", "Discussione: " + thread.getTitle());
            request.setAttribute("viewUrl", "threadManagement/threadView");
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

    public static void deleteComment(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        List<Message> messages;
        List<User> chatters;

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

            int threadId = Integer.parseInt(request.getParameter("threadId"));
            int messageId = Integer.parseInt(request.getParameter("messageId"));
            // cancello messaggio dal DB
            MessageDAO messageDAO = daoFactory.getMessageDAO();
            messageDAO.deleteMessage(messageId);

            // vista
            messages = messageDAO.getThreadMessages(threadId);

            ThreadDAO threadDAO = daoFactory.getThreadDAO();
            Thread thread = threadDAO.getThreadById(threadId);

            UserDAO userDAO = daoFactory.getUserDAO();
            chatters = userDAO.getChatters(messages);
            List<String> formattedCreationTimestamps = threadDAO.getFormattedCreationTimestamps(messages);
            User threadUser = userDAO.getUserById(thread.getUserId());

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("thread", thread);
            request.setAttribute("threadUser", threadUser);
            request.setAttribute("messages", messages);
            request.setAttribute("chatters", chatters);
            request.setAttribute("formattedLastReply", formatLocalDateTime(thread.getTimestampLastReply()));
            request.setAttribute("threadFormattedCreationTimestamp", formatLocalDateTime(thread.getCreationTimestamp()));
            request.setAttribute("formattedCreationTimestamps", formattedCreationTimestamps);
            request.setAttribute("menuActiveLink", "Discussione: " + thread.getTitle());
            request.setAttribute("viewUrl", "threadManagement/threadView");
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

