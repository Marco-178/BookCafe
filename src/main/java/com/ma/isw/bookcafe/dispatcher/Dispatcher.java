package com.ma.isw.bookcafe.dispatcher;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.rmi.ServerException;
import java.util.List;
import java.util.logging.Level;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import com.ma.isw.bookcafe.services.logservice.LogService;
import jakarta.servlet.http.Part;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.servlet5.JakartaServletFileUpload;


@WebServlet(name = "Dispatcher", urlPatterns = {"/Dispatcher"})
@MultipartConfig
public class Dispatcher extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String controllerAction = null;
            boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                // passa il file al controller
                Part pictureUploadPart = request.getPart("pictureUpload");
                if (pictureUploadPart != null && pictureUploadPart.getSize() > 0) {
                    request.setAttribute("pictureUpload", pictureUploadPart);
                }

                // estrae controller
                controllerAction = request.getParameter("controllerAction");
                if (controllerAction == null) {
                    controllerAction = "HomeManagement.view";
                }

                // passa al controller gli altri attributi
                for (String fieldName : request.getParameterMap().keySet()) { // restituisce coppie chiave-valore dei parametri della richiesta
                    if (!fieldName.equals("pictureUpload")) {
                        request.setAttribute(fieldName, request.getParameter(fieldName));
                    }
                }
            } else {
                // richieste non multipart
                controllerAction = request.getParameter("controllerAction");
            }

            if (controllerAction==null) controllerAction="HomeManagement.view";

            String[] splittedAction=controllerAction.split("\\.");
            Class<?> controllerClass=Class.forName("com.ma.isw.bookcafe.controller." + splittedAction[0]);
            Method controllerMethod=controllerClass.getMethod(splittedAction[1], HttpServletRequest.class, HttpServletResponse.class);
            LogService.getApplicationLogger().log(Level.INFO,splittedAction[0]+" "+splittedAction[1]);
            controllerMethod.invoke(null,request,response);

            String viewUrl=(String)request.getAttribute("viewUrl");
            RequestDispatcher view=request.getRequestDispatcher("jsp/"+viewUrl+".jsp");
            view.forward(request,response);

        } catch (Exception e) {
            e.printStackTrace(out);
            throw new ServerException("Dispatcher Servlet Error",e);

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
