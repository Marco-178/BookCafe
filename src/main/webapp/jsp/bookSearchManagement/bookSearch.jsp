<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Login";
%>

<html>
    <head>

    </head>
    <body>

    </body>
</html>