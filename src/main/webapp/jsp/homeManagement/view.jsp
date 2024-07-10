<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.ma.isw.bookcafe.model.mo.User"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
%>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
</head>
<body>
<%@include file="/include/header.inc"%>
<main>
    <%if (loggedOn) {%>
    Benvenuto <%=loggedUser.getUsername()%>!<br/>
    <%} else {%>
    Benvenuto.
    <%}%>
</main>
<%@include file="/include/footer.inc"%>
</html>
