package com.ma.isw.bookcafe.controller;

import com.ma.isw.bookcafe.model.dao.ThreadDAO;
import com.ma.isw.bookcafe.model.dao.UserDAO;
import com.ma.isw.bookcafe.model.dao.DAOFactory;
import com.ma.isw.bookcafe.model.dao.UserMessageDAO;
import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.model.mo.Thread;
import com.ma.isw.bookcafe.model.mo.UserMessage;
import com.ma.isw.bookcafe.services.config.Configuration;
import com.ma.isw.bookcafe.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            UserDAO userDAO = daoFactory.getUserDAO();
            users = userDAO.getAllUsers();

            daoFactory.commitTransaction();

            request.setAttribute("threadsList", threads);
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

    public static void viewThread(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory daoFactory = null;
        List<UserMessage> messages;
        List<User> users;

        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            int threadId = Integer.parseInt(request.getParameter("threadId"));

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            ThreadDAO threadDAO = daoFactory.getThreadDAO();
            Thread thread = threadDAO.getThreadById(threadId);

            UserMessageDAO messageDAO = daoFactory.getMessageDAO();
            messages = messageDAO.getAllMessages(threadId);

            UserDAO userDAO = daoFactory.getUserDAO();
            users = userDAO.getAllUsers();

            daoFactory.commitTransaction();

            request.setAttribute("thread", thread);
            request.setAttribute("messagesList", messages);
            request.setAttribute("usersList", users);
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

